import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8080/api' // Aapka Spring Boot port
});

export default API;