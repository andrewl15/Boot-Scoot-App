import axios from 'axios';

/**
 * This service class is used to interact with the server's Authentication API.
 * All methods return a Promise so that the calling code can handle both success and
 * error responses appropriately.
 */
export default {

    searchDances(searchTerm) {
        return axios.get('/danceSearch/search', {
            params: {
                query: searchTerm
            }
        });
    }


}
