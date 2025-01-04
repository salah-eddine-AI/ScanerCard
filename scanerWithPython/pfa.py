import cv2
import pytesseract
import re
import json
import numpy as np
from flask import Flask, request, jsonify
import os

app = Flask(__name__)

pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def preprocess_image(image_path):
    image = cv2.imread(image_path)

    if image is None:
        raise FileNotFoundError(f"Image not found at '{image_path}'.")
    image = cv2.resize(image, None, fx=3, fy=3, interpolation=cv2.INTER_CUBIC)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    blurred = cv2.GaussianBlur(gray, (5, 5), 0)

    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
    enhanced = clahe.apply(blurred)
    _, binary = cv2.threshold(enhanced, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

   
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
    processed = cv2.dilate(binary, kernel, iterations=1)

   
    kernel_sharpening = np.array([[0, -1, 0], [-1, 5,-1], [0, -1, 0]])
    sharpened = cv2.filter2D(processed, -1, kernel_sharpening)

    return sharpened


def clean_text_for_date_extraction(text):

    text = re.sub(r'[^a-zA-Z0-9\sàâäéèêëîïôöùûüç:/\n-]', '', text)  
    text = re.sub(r'\s+', ' ', text) 
    text = text.strip()

    text = re.sub(r'(\d{2})5/(\d{2}/\d{4})', r'0\1/05/\2', text)  

    
    text = re.sub(r'(\d{2})/(\d{2})/(\d{4})', r'\1/\2/\3', text) 

    return text

def extract_text_from_image(image_path):

    processed_image = preprocess_image(image_path)

    custom_config = r'--oem 1 --psm 6'  
    try:
        text = pytesseract.image_to_string(processed_image, lang='fra', config=custom_config)
    except pytesseract.TesseractError as e:
        print(f"Tesseract Error: {e}")
        return ""

    return text

def parse_french_info(text):
    info = {}

    cleaned_text = clean_text_for_date_extraction(text)
    print(f"Cleaned text for Date de Validité extraction: {cleaned_text}")

  
    cin_pattern = r"\b[A-Z]{1,3}\s*\d{6,8}\b"
    cin_match = re.search(cin_pattern, cleaned_text)
    if cin_match:
        info["CIN"] = cin_match.group(0).replace(" ", "")

  
    name_pattern = r"(?i)(?:Nom|NOM|Nom complet)\s*[:\-]?\s*([\w\s]+)"
    name_match = re.search(name_pattern, cleaned_text)
    if name_match:
        info["Nom"] = name_match.group(1).strip()

   
    first_name_pattern = r"(?i)(?:Prénom|PRENOM)\s*[:\-]?\s*([\w]+)"
    first_name_match = re.search(first_name_pattern, cleaned_text)
    if first_name_match:
        info["Prénom"] = first_name_match.group(1).strip()

    dob_pattern = r"(?i)(?:Née? le|Naissance)\s*[:\-]?\s*(\d{2}/\d{2}/\d{4})"
    dob_match = re.search(dob_pattern, cleaned_text)
    if dob_match:
        info["Date de Naissance"] = dob_match.group(1)
    else:
    
        potential_dob_pattern = r"\d{2}/\d{2}/\d{4}"
        potential_dob_match = re.search(potential_dob_pattern, cleaned_text)
        if potential_dob_match:
            info["Date de Naissance"] = potential_dob_match.group(0)

    expiry_pattern = r"(?i)(?:Valable\s*jusqu\s*au|Expiration)[\s:]*([\d/]{10})"
    expiry_match = re.search(expiry_pattern, cleaned_text)
    if expiry_match:
        info["Date de Validité"] = expiry_match.group(1)
        
    ville_pattern = r"(?i)(?:à\s+|ville\s*[:\-]?\s*)([A-Za-zÀ-ÿ\s\-]+)(?=\s*(?:\d{4}|\b))"
    ville_match = re.search(ville_pattern, cleaned_text)
    if ville_match:
        info["Ville"] = ville_match.group(1).strip()

    return info


@app.route('/extract', methods=['POST'])
def extract_information():

    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400

    file = request.files['image']

    upload_folder = 'uploads'
    if not os.path.exists(upload_folder):
        os.makedirs(upload_folder)

    file_path = os.path.join(upload_folder, file.filename)
    file.save(file_path)

    extracted_text = extract_text_from_image(file_path)
    extracted_info = parse_french_info(extracted_text)
    os.remove(file_path)

    return jsonify({
        "extracted_text": extracted_text,
        "extracted_information": extracted_info
    })

if __name__ == "__main__":
    app.run(debug=True)
