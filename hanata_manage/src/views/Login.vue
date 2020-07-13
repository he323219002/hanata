<template>
  <div class="login_container">
    <div class="login_box">
      <div class="logo_box">
        <img src="../assets/pngtree-fish-logo-icon-vector-png-image_1127380.jpg">
      </div>

      <div class="login_input">
        <el-form label-width="80px">
          <el-form-item label="登录账号">
            <el-input @keyup.enter.native=login() v-model="loginFrom.username"></el-input>
          </el-form-item>

          <el-form-item label="登录密码">
            <el-input @keyup.enter.native=login() type="password" v-model="loginFrom.password"></el-input>
          </el-form-item>

          <el-row>
            <el-col :span="18">
              <el-form-item label="验证码">
                <el-input @keyup.enter.native=login() v-model="loginFrom.captcha"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <img :src="captcha_src" @click="reloadPic()">
            </el-col>

          </el-row>

          <el-button type="primary" @click="login()">登录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  import {randomStr} from "../utils/utils"
  import {postRequest} from "../utils/api"
  let catchId = randomStr();
  export default {
    data() {
      return {
        // captcha_src:"http://121.40.70.48/api/captcha?formId="+catchId,
        captcha_src:"http://www.hanata.top/api/captcha?formId="+catchId,
        loginFrom:{
          username:'',
          password:'',
          captcha:'',
          formId:catchId,
        }
      }
    },
    methods: {
      async login() {
        let {data:res} = await postRequest('user/login',this.loginFrom);
        console.log(res);
        if (res.code===0){
          window.sessionStorage.setItem('token',res.data.token)
          this.$router.push({name:'Home'})
        }
        else{
          this.errorMsg(res.msg)
          this.loginFrom.password=''
          this.loginFrom.captcha=''
          this.reloadPic()
        }
      },
      reloadPic(){
        this.captcha_src=this.captcha_src+"&"+Math.random();
      },
      errorMsg(msg){
        this.$message({
          message:msg,
          type:'error'
        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .login_container {
    background-color: #2b4b6b;
    height: 100%;
  }

  .login_input{
    position: absolute;
    transform: translate(0,30%);
    width: 100%;
    padding: 10px;
    box-sizing: border-box;
  }

  .login_box {
    width: 450px;
    height: 350px;
    background-color: #ffffff;
    border: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    .logo_box{
      height: 130px;
      width: 130px;
      border: 1px solid #eee;
      border-radius: 50%;
      padding: 2px;
      background-color: #eeeeee;
      box-shadow: 0 0 10px #ddd;
      position: absolute;
      left: 50%;
      transform: translate(-50%,-50%);
      img{
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
    }
  }
</style>