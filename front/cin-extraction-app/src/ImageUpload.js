import React, { useState } from 'react';
import axios from 'axios';

const ImageUpload = () => {
  const [image, setImage] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [extractedInfo, setExtractedInfo] = useState(null);

  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!image) {
      alert('Please select an image.');
      return;
    }

    setIsLoading(true); // Start loading
    const formData = new FormData();
    formData.append('image', image);

    try {
      const response = await axios.post('http://127.0.0.1:5000/extract', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setTimeout(() => {
        setExtractedInfo(response.data.extracted_information); // Extract only the relevant data
        setIsLoading(false); // Stop loading after delay
      }, 6000); // Simulate 6-second loading time
    } catch (error) {
      alert('Failed to upload image. Please try again.');
      setIsLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Upload CIN Image</h2>
      <input type="file" accept="image/*" onChange={handleImageChange} />
      <button onClick={handleUpload} disabled={isLoading}>Upload</button>
      {isLoading && <p>Loading... Please wait.</p>}
      {!isLoading && extractedInfo && (
        <div>
          <h3>Extracted Information</h3>
          <ul>
            {Object.entries(extractedInfo).map(([key, value]) => (
              <li key={key}>
                <strong>{key}:</strong> {value}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default ImageUpload;
