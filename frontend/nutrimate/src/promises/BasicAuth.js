import axios from  "axios";

var config = {
  // baseURL: 'https://localhost:8443/',
  baseURL: 'http://localhost:8080/',    
};

const BasicAuth = axios.create(config);

export default BasicAuth;
