import axios from "axios";

const REST_API_URL = 'http://localhost:9000/api/v1';

/* 리스트 출력 API*/
export const listLunch = () => axios.get(REST_API_URL);
export const Choose = () => axios.post(REST_API_CHOOSE);
/* 삭제 */
export const deleteId = (id) => axios.delete(REST_API_URL + "/" + id);
export const updateLunch = (id, lunch) => axios.put(REST_API_URL + "/" + id, lunch);
export const getLunch = (id) => axios.get(REST_API_URL + "/" + id);
export const insertLunch = (lunch) => axios.post(REST_API_INSERT,lunch);
export const listLogLunch = () => axios.get(REST_API_LOG);
export const saveLog = () => axios.post(REST_API_SAVE);
export const deleteLog = (id) => axios.delete(REST_API_LOG + "/" + id);