<template>
  <div class="login">
    <img :src="require('../assets/osaka3.jpg')" style="position: absolute;width: 100%;z-index: 0">

    <div class="login_box">

    </div>
    <div class="login_form">
      <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="17">
            <el-form-item label="验证码">
              <el-input @keyup.enter.native=login() v-model="loginForm.captcha"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6" :offset="1">
            <img :src="captcha_src" @click="reloadPic()">
          </el-col>

        </el-row>

        <el-form-item>
          <el-button type="primary" @click="login()">登录</el-button>
          <el-button type="primary" @click="regist()">注册</el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script>
  import {checkToken, randomStr} from "../utils/utils"
  import {postRequest} from "../utils/api";

  let catchId = randomStr();
  export default {
    data() {
      return {

        captcha_src: "http://127.0.0.1:8000/api/captcha?formId=" + catchId,
        loginForm: {
          username: '',
          password: '',
          captcha: '',
          formId: catchId,
        },
        rules: {
          username: [{required: true, message: '用户名必填', trigger: 'blur'}],
          password: [{required: true, message: '密码必填', trigger: 'blur'}],
          captcha: [{required: true, message: '验证码必填', trigger: 'blur'}]
        }
      };
    },
    methods: {
      async login() {
        let {data:res} = await postRequest('user/login',this.loginForm);
        if (res.code===0){
          window.sessionStorage.setItem('token',res.data.token)
          window.sessionStorage.setItem('mail',res.data.mail)
          window.sessionStorage.setItem('nickname',res.data.nickname)
          window.sessionStorage.setItem('avatar',res.data.avatar)
          this.$router.push({name:'Index'})
        }
        else{
          this.errorMsg(res.msg);
          this.loginForm.password='';
          this.loginForm.captcha='';
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
      },
      regist(){
        this.$router.push({name:'Register'})
      }
    }
  }
</script>

<style lang="less" scoped>
  .login {
    height: 100%;
    background-color: #000000;
    position: relative;
  }

  .login_form {
    width: 450px;
    height: 300px;
    border: 1px solid #eeeeee;
    box-shadow: 0 0 10px #dddddd;
    border-radius: 5%;
    padding-top: 80px;
    padding-right: 30px;
    text-align: left;
    left: 50%;
    top: 50%;
    background-color: white;
    position: absolute;

    z-index: 1;
    /*margin-top: 30px;*/
    transform: translate(-50%, -60%);
    opacity: 100%;
    margin-left: 10px;
  }
</style>