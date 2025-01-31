import axios from "axios";

//createUser
export const registerRequest = (props) => {
    return axios.post(`/users`, props);
};

//getUser
export const getUserByIdRequest = (id) => {
    return axios.get(`/users/${id}`, id);
};

//updateUser
export const editUserRequest = (data) => {
    return axios.patch(`/users/${data.id}`,
        {
        language: data.language,
        newUsername: data.newUsername,
        },
        {
            headers: {
                'Authorization': 'Bearer ' + (localStorage.getItem('token') || sessionStorage.getItem('token')) || '',
            },
        }
    );
};

export const createPasswordCodeRequest = (email) => {
    return axios.post(`/users`, { email });
};

//updatePassword
export const updatePasswordRequest = (data) => {
    return axios.patch(`/users/${data.password_token}`,
        {
          newPassword: data.newPassword,
        },
    );
};

//verifyUser
export const verifyUserRequest = (verificationCode) => {
    return axios.post(`/users`, { verificationCode });
};

//createLocation
export const createLocationRequest = (data) => {
    return axios.post(`/users/${data.id}/locations`,
      { locationString: data.locationString });
}

//getLocation
export const getLocationByIdRequest = ({ id, location_id }) => {
  return axios.get(`/users/${id}/locations/${location_id}`);
}

//getLocations
export const getLocationsRequest = ({ id, publicationURN }) => {
  return axios.get(`/users/${id}/locations`,
    {
      params: {
        publicationURN: publicationURN,
      },
    });
};

//removeLocation
export const deleteLocationRequest = ({ id, location_id }) => {
  return axios.delete(`/users/${id}/locations/${location_id}`);
};

//createReview
export const createReviewRequest = ({ id, exchange_id, description, rating}) => {
    return axios.post(`/users/${data.id}/reviews`,
        {
          description: description,
            rating: rating,
        },
        {
            params: {
                exchange_id: exchange_id,
            },
            headers: {
                'Authorization': 'Bearer ' + (localStorage.getItem('token') || sessionStorage.getItem('token')) || '',
            },
        }
    );
};

//getReview
export const getReviewByIdRequest = ({ id, review_id }) => {
    return axios.get(`/users/${id}/reviews/${review_id}`);
};

//getReviews
export const getReviewsRequest = ({ id, page }) => {
    return axios.get(`/users/${id}/reviews`,
        {
            params: {
                page: page,
            },
            headers: {
              'Authorization': 'Bearer ' + (localStorage.getItem('token') || sessionStorage.getItem('token')) || '',
            },
        });
};
