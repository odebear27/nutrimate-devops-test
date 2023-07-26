import axios from  "axios";

//For Development
// var config = {
//   // baseURL: 'https://localhost:8443/',
//   baseURL: 'http://localhost:8080/',    
// };

//For Production
var config = {
  // baseURL: 'https://localhost:8443/',
  // baseURL: 'http://localhost:8080/',    
};

const BasicAuth = axios.create(config);

export default BasicAuth;
