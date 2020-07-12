// export const isNotNullORBlank = (...args)=> {
//   for (var i = 0; i < args.length; i++) {
//     var argument = args[i];
//     if (argument == null || argument == '' || argument == undefined) {
//       return false;
//     }
//   }
//   return true;
// }
export const randomStr = () => {

  return Math.random().toString().slice(-6);
}


export const getToken = () => {
  let token = window.sessionStorage.getItem("token")
  if (token == null) {
    return ""
  } else {
    return token
  }
}




import {Message} from "element-ui";
import router from '../router'

export const checkToken = (res) => {
  console.log(res);
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
    setInterval(() => {
      router.push({name: "Login"})
    }, 1000)
  }
}