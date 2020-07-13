import Vue from 'vue'


import axios from 'axios'

// axios.defaults.baseURL = 'http://127.0.0.1:8000/api/'
axios.defaults.baseURL = 'http://www.hanata.top/api/'
export let picBaseURL = 'https://hanata.oss-cn-hangzhou.aliyuncs.com/'
Vue.prototype.$http = axios

export const postRequest = (url, params={}) =>{
  return axios({
    headers:{
      'Authorization':getToken(),
      'Content-Type':'application/json',

    },
    method:'post',
    url:`${axios.defaults.baseURL}${url}`,
    data:params,
  });
}


export const getToken = () => {
  let token = window.sessionStorage.getItem("token")
  if (token == null) {
    return ""
  } else {
    return token
  }
}