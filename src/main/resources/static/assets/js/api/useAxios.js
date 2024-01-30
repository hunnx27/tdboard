// import axios from 'axios'
// import handler from './res-handler.js'

const URI_PREPENDER = ''
const wrap = (url) => `${URI_PREPENDER}${url}`
const appendAuth = (config) => {
  const token = getCookieValue('accessToken');
  const authorization = token ? `Bearer ${getCookieValue('accessToken')}` : null;
  // console.log('token',token)
  if (token) {
    if (!config) config = { headers: {} }
    if (!config.headers) config.headers = {}
    config.headers.Authorization = authorization // 추후 토큰 세팅 필요
  }
  return config
}
const appendMultipart = (config) => {
  if (!config) config = { headers: {} }
  if (!config.headers) config.headers = {}
  // config.headers.ContentType = 'multipart/form-data'
  // config.headers.accept= 'application/json'
  config.headers['Content-Type'] = 'multipart/form-data';  // 수정된 부분
  config.headers['Accept'] = 'application/json';  // 추가된 부분
  return config
}

export default {
  async get (url, params, success, fail = err => err.response?.data.message, config) {
    const token = getCookieValue('accessToken');
    const authorization = token ? `Bearer ${getCookieValue('accessToken')}` : null;
    const api = axios.create({
      headers: {
        Authorization: authorization,
        responseType: 'json'
      },
    });
    setApiInterceptor(api);
    await api.get(wrap(url),{params}, appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async post (url, body, success, fail = err => err.response?.data.message, config) {
    const api = axios.create();
    setApiInterceptor(api);
    await api.post(wrap(url), body, appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async put (url, body, success, fail = err => err.response?.data.message, config) {
    const api = axios.create();
    setApiInterceptor(api);
    await api.put(wrap(url), body, appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async upload (url, body, progress, success, fail) {
    var formData = new FormData()
    if (body.constructor === Object) {
      for (let key in body) {
        formData.append(key, body[key])
      }
    } else if (body.constructor === Array) {
      body.forEach(b => formData.append(b[0], b[1]))
    } else {
      console.error('unkown type')
    }
    const api = axios.create();
    setApiInterceptor(api);
    await api.post(wrap(url), formData, {
      headers: {
        'Content-Type': 'multipart/formdata; charset=utf-8',
        'Accept': '*/*'
      },
      onUploadProgress: e => { progress(e.loaded) }
    })
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async delete (url, success, fail = err => err.response.data.message, config) {
    const api = axios.create();
    await api.delete(wrap(url), appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async postMultipart (url, form, success, fail, config) {
    const configAuth = appendAuth(config);
    const api = axios.create();
    setApiInterceptor(api);
    await api.post(wrap(url), form, appendMultipart(configAuth))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async putMultipart (url, form, success, fail, config) {
    const configAuth = appendAuth(config);
    const api = axios.create();
    setApiInterceptor(api);
    await api.put(wrap(url), form, appendMultipart(configAuth))
      .then((res)=>success(res.data))
      .catch(fail)
  },
}

const setApiInterceptor = (axiosInstance)=>{
  axiosInstance.interceptors.request.use(
      config => {
        console.log('Request Interceptor:', config);
        return config;
      },
      error => {
        console.log('error : ', error);
        return Promise.reject(error);
      }
  );
  axiosInstance.interceptors.response.use(
      response => {
        console.log('Response Interceptor:', response);
        return response;
      },
      error=>{
        console.log('Response Error:', error);
        return Promise.reject(error);
      }
  );
}
// 쿠키에서 값을 가져오는 함수
function getCookieValue(cookieName) {
  const cookies = document.cookie.split(';');
  
  for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();

      if (cookie.startsWith(cookieName + '=')) {
          // 쿠키 값을 디코딩하여 반환합니다.
          const cookieValue = decodeURIComponent(cookie.substring(cookieName.length + 1));
          return cookieValue;
      }
  }

  return null;
}