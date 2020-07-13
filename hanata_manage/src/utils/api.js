import Vue from 'vue'

import axios from 'axios'
import {getToken} from "../utils/utils"


axios.defaults.baseURL = 'http://www.hanata.top/api/'
// axios.defaults.baseURL = 'http://127.0.0.1:8000/api/'
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


export const uploadRequest = (url,param={}) => {
    return axios({
      headers:{
        'Authorization':getToken(),
        'Content-Type':'multipart/form-data'
      },
      method:'post',
      url:`${axios.defaults.baseURL}${url}`,
      data:param
    })
}
