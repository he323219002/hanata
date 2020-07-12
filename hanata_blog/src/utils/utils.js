

import {Message} from "element-ui";
import router from '../router'

export const checkToken = (res) => {
  let msg = res.msg
  if (msg === "未获取到token" || msg === "token解析异常" || msg === "用户不存在") {
    Message({
      message: 'token错误，请重新登录',
      type: 'warning'
    });
    setInterval(() => {
      router.push({name: "Login"})
    }, 1000)
  } else if (res.code !== 0) {
    Message({
      message: res.msg,
      type: 'warning'
    });
    // setInterval(() => {
    //   router.push({name: "Login"})
    // }, 1000)
  }
}

export const randomStr = () => {

  return Math.random().toString().slice(-6);
}
