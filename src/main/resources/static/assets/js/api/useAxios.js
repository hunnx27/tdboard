// import axios from 'axios'
// import handler from './res-handler.js'

const URI_PREPENDER = ''
const wrap = (url) => `${URI_PREPENDER}${url}`
const appendAuth = (config) => {
  const token = 'test'//store.getters.token
  if (token) {
    if (!config) config = { headers: {} }
    if (!config.headers) config.headers = {}
    config.headers.Authorization = token // 추후 토큰 세팅 필요
  }
  return config
}
const appendMultipart = (config) => {
  if (!config) config = { headers: {} }
  if (!config.headers) config.headers = {}
  config.headers.ContentType = 'multipart/form-data'
  return config
}

export default {
  async get (url, success, fail = err => err.response?.data.message, config) {
    await axios.get(wrap(url), appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async post (url, body, success, fail = err => err.response?.data.message, config) {
    await axios.post(wrap(url), body, appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async put (url, body, success, fail = err => err.response?.data.message, config) {
    await axios.put(wrap(url), body, appendAuth(config))
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
    await axios.post(wrap(url), formData, {
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
    await axios.delete(wrap(url), appendAuth(config))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async postMultipart (url, form, success, fail, config) {
    const configAuth = appendAuth(config);
    await axios.post(wrap(url), form, appendMultipart(configAuth))
      .then((res)=>success(res.data))
      .catch(fail)
  },
  async putMultipart (url, form, success, fail, config) {
    const configAuth = appendAuth(config);
    await axios.put(wrap(url), form, appendMultipart(configAuth))
      .then((res)=>success(res.data))
      .catch(fail)
  },
}