<template>
  <div class="reset-password">
    <el-form :model="passwordForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="passwordForm.password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="passwordForm.checkPass" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="submitForm">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";

  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.passwordForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.passwordForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        passwordForm: {
          password: '',
          checkPass: '',
          token:this.$route.query.token,
          userID:this.$route.query.userId
        },
        rules: {
          password: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
        }
      };
    },
    methods: {
      async submitForm() {
        let {data:res} = await postRequest('user/setPassword',this.passwordForm);
        console.log(res);
        checkToken(res)
        if (res.code===0){
          this.$message({
            type: 'success',
            message: '修改成功'
          });
        }

        setTimeout(this.$router.push({name:"Index"}),2000)
      },
    }
  }
</script>

<style scoped>

</style>