<template>
  <div class="newUpdate">
    <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
      <el-form-item label="版本号" prop="version" >
        <el-input  v-model="updateForm.version"></el-input>
      </el-form-item>

      <el-form-item label="更新内容" prop="version" >
        <el-input  v-model="updateForm.content"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="createUpdate">创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "NewUpdate",
    data() {
      return {
        updateForm:{
          version:'',
          content:''
        },
        rules:{
          version:[{
            required:true,
            message:'版本号必填',
            trigger:'blur'
          }],
          content:[{
            required:true,
            message:'更新内容必填',
            trigger:'blur'
          }]
        }
      }
    },
    methods:{
      async createUpdate(){
        let {data:res} = await postRequest('update/new',this.updateForm);
        checkToken(res);
        if (res.code!==0){
          this.$message({
            message:res.msg,
            type:'warning'
          })
        }
        this.$router.push({name:'Update'})
      }
    }
  }
</script>

<style scoped>
  .newUpdate{
    margin: 5px;
  }
</style>