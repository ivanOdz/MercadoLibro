import axios from 'axios';

export default axios.create({
    timeout: 5000,
    baseURL: process.env.PUBLIC_URL,
    headers: {
        Accept: 'application/json',
    },
});
