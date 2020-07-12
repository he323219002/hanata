<template>
  <div class="newLabel">
    <el-form ref="labelForm" :model="labelForm" :rules="rules" label-width="80px">
      <el-form-item label="label名称" prop="name" label-width="auto">
        <el-input @keyup.enter.native="createLabel" v-model="labelForm.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="createLabel">更新</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "UpdateLabel",
    data(){
      return{
        labelForm:{
          id:this.$store.state.labelId,
          name:this.$store.state.labelName
        },
        rules:{
          name:[{
            required:true,
            message:'名称必填',
            trigger:'blur'
          }]
        }
      }
    },
    methods:{
      createLabel(){
        this.$refs.labelForm.validate(valid=>{
          if(!valid){
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          }else{
            this.submitLabel()
          }
        })
      },
      async submitLabel(){
        let {data:res} = await postRequest('label/update',this.labelForm);
        console.log(res);
        checkToken(res)
        this.$router.push({name:'Label'})
      }
    }
  }
</script>

<style lang="less" scoped>
  .newLabel{
    margin: 20px;
  }
</style>