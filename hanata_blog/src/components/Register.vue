<template>
  <div class="register">
    <img :src="require('../assets/outdoors.jpg')" style="position: absolute;width: 100%;z-index: 0">

    <div class="register_box">

    </div>
    <div class="register_form">
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="100px">
        <el-form-item label="用户名" prop="username" :error="usernameError" required>
          <el-input v-model="registerForm.username" @blur="checkMsg('1',registerForm.username)"></el-input>
        </el-form-item>



        <el-form-item label="密码" prop="password" required>
          <el-input type="password" v-model="registerForm.password"></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="mail" :error="mailError" required>
          <el-input  v-model="registerForm.mail" @blur="checkMsg('3',registerForm.mail)"></el-input>
        </el-form-item>

        <el-form-item label="昵称" prop="nickname" :error="nicknameError" required>
          <el-input  v-model="registerForm.nickname" @blur="checkMsg('2',registerForm.nickname)"></el-input>
        </el-form-item>

        <el-form-item label="头像" prop="avatar">
          <div class="upload">
            <el-upload
                    class="avatar-uploader"
                    action="http://127.0.0.1:8000/api/upload/"
                    :show-file-list="false"
                    :auto-upload="true"
                    :headers="token"
                    :data="extraData"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload">
              <img v-if="registerForm.avatar" :src="registerForm.avatar" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>

        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-select v-model="registerForm.gender" placeholder="请选择">
            <el-option
                    v-for="item in genderOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="register()">提交</el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script>
  import {postRequest,picBaseURL} from "../utils/api";
  import {getToken} from "../utils/api";


  export default {
    data() {
      return {
        registerForm: {
          username: '',
          password: '',
          nickname: '',
          gender: '',
          mail: '',
          avatar: '',
        },
        extraData: {
          'fileType': 'pic',
        },
        token: {
          'Authorization': getToken()
        },
        usernameError: '',
        nicknameError: '',
        mailError: '',
        genderOptions:[{'label':'女','value':'0'},{'label':'男','value':'1'}],


        rules: {
          // username: [{required: true, message: '用户名必填', trigger: 'blur'}],
          password: [{required: true, message: '密码必填', trigger: 'blur'}],
          // nickname: [{required: true, message: '昵称必填', trigger: 'blur'}],
          // mail: [{required: true, message: '邮箱必填', trigger: 'blur'}],
          gender: [{required: true, message: '性别必填', trigger: 'blur'}],
        }
      };
    },
    methods: {
      async register() {
        console.log(this.registerForm);
        let {data: res} = await postRequest('user/reg', this.registerForm);
        if (res.code === 0) {
          this.$message({
            message:'恭喜你，注册成功',
            type:'success',
          });
          setTimeout(this.$router.push({name: 'Login'}),1000)
        } else {
          this.$message({
            message:res.msg,
            type:'error',
          });
        }
      },
      reloadPic() {
        this.captcha_src = this.captcha_src + "&" + Math.random();
      },

      async checkMsg(type, value) {
        let {data: res} = await postRequest('user/check', {'type': type, 'value': value});
        if (!value){
          switch (type) {
            case '1':
              this.usernameError = "该项必填";
              break;
            case '2':
              this.nicknameError = "昵称必填";
              break;
            case '3':
              this.mailError = "邮箱必填";
              break;
          }
        }
        else{
          this.usernameError=this.nicknameError=this.mailError=''
        }
        if (res.code === 1) {
          switch (type) {
            case '1':
              this.usernameError = res.msg;
              break;
            case '2':
              this.nicknameError = res.msg;
              break;
            case '3':
              this.mailError = res.msg;
              break;
          }
        }
      },
      handleAvatarSuccess(res, file) {
        console.log(file);
        this.registerForm.avatar = picBaseURL + file.response.data.filePath;
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      }
    }
  }
</script>

<style lang="less" scoped>
  .register {
    height: 100%;
    background-color: #000000;
    position: relative;
  }

  .register_form {
    width: 450px;
    height: 500px;
    border: 1px solid #eeeeee;
    box-shadow: 0 0 10px #dddddd;
    border-radius: 5%;
    padding-top: 50px;
    padding-right: 30px;
    text-align: left;
    left: 50%;
    top: 50%;
    background-color: white;
    position: absolute;

    z-index: 1;
    /*margin-top: 30px;*/
    transform: translate(-50%, -65%);
    opacity: 100%;
    margin-left: 10px;
  }



  .avatar-uploader .el-upload {
    border: 1px dashed black;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 60px;
    height: 60px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 60px;
    height: 60px;
    display: block;
  }
</style>