<template>
  <div class="account">
    <el-form :model="accountForm" status-icon :rules="rules" label-width="100px" class="demo-ruleForm">
      <el-form-item label="昵称"  prop="nickname">
        <el-input  v-model="accountForm.nickname" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="mail">
        <el-input v-model="accountForm.mail" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="头像上传">
        <img :src="accountForm.avatar" class="avatar">
        <el-upload
                class="avatar-uploader"
                action="http://127.0.0.1:8000/api/upload/"
                :show-file-list="false"
                :auto-upload="true"
                :headers="token"
                :data="extraData"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
          <i class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

      </el-form-item>



      <el-form-item>
        <el-button type="primary" @click="updateUser">提交</el-button>
        <el-button type="default" plain @click="resetpwd">重置密码/忘记密码</el-button>

      </el-form-item>

    </el-form>
  </div>
</template>

<script>
  import {picBaseURL, postRequest} from "../utils/api";
  import {getToken} from "../utils/api";
  import {checkToken} from "../utils/utils";


  export default {
    name: "Account",
    mounted() {
      let avatar = window.sessionStorage.getItem('avatar');
      if (avatar){
        this.accountForm.avatar=avatar
      }

    },
    data(){
      return{
        accountForm:{
          mail:window.sessionStorage.getItem("mail"),
          nickname:window.sessionStorage.getItem("nickname"),
          avatar:require('../assets/default-avatar-png.png')
        },
        rules:{
          nickname:[{required:true,trigger:'blur',message:'昵称必填'}],
          mail:[{required:true,trigger:'blur',message:'邮箱必填'}],
        },
        extraData: {
          'fileType': 'pic',
        },
        token: {
          'Authorization': getToken()
        },
      }
    },
    methods:{
      handleAvatarSuccess(res, file) {
        this.accountForm.avatar = picBaseURL + file.response.data.filePath;
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
      },

      async updateUser(){
        let {data:res} = await postRequest('user/update',this.accountForm);
        checkToken(res)
        window.sessionStorage.setItem("avatar",this.accountForm.avatar)
        window.sessionStorage.setItem("mail",this.accountForm.mail)
        window.sessionStorage.setItem("nickname",this.accountForm.nickname)
        this.$router.push({name:"Index"})
      },
      resetpwd() {
        this.$prompt('请输入邮箱', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
          inputErrorMessage: '邮箱格式不正确'
        }).then(({ value }) => {
          postRequest("user/reset",{'mail':value}).then((res)=>{
            if (res.code===0){
              this.$message({
                type: 'success',
                message: res.data
              });
            }
            else{
              this.$message({
                type: 'error',
                message: res.data.msg
              });
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });
        });
      }

    },
    watch:{
      'accountForm.avatar'(newVal,oldVal){
        this.accountForm.avatar=newVal
      }
    }
  }
</script>

<style lang="less" scoped>
  .account{
    margin: 20px;
    width: 80%;
  }
  .avatar{
    height: 80px;
    width: 80px;
  }
</style>