import React from 'react';
import ReactDOM from 'react-dom';
import './reset.css';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';


import axios from 'axios';

// For GET requests
axios.interceptors.request.use(
  (req) => {
     if(!req.headers.Authorization){   
      req.headers.Authorization = 'Bearer ' + localStorage.getItem('token');
     }
     return req;
  },
  (err) => {
     return Promise.reject(err);
  }
);

// For POST requests
axios.interceptors.response.use(
  (res) => {
    if (res.status === 401) {
      console.log('Unauthorized 1');
   }
     return res;
  },
  (err) => {
    if (err.status === 401) {
      console.log('Unauthorized 2');
   }
     return Promise.reject(err);
  }
);


ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
