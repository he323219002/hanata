<template>
  <div class="newTag">
    <el-form ref="tagForm" :model="tagForm" :rules="rules" label-width="80px">
      <el-form-item label="类别名称" prop="name" >
        <el-input  v-model="tagForm.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="createTag" @keyup.enter.native="createTag">创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "NewTag",
    data(){
      return{
        tagForm:{
          name:''
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
      createTag(){
        this.$refs.tagForm.validate(valid=>{
          if(!valid){
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          }else{
            this.submitTag()
          }
        })
      },
      async submitTag(){
        let {data:res} = await postRequest('tag/new',this.tagForm);
        console.log(res);
        checkToken(res)
        this.$router.push({name:'_Tag'})
      }
    }
  }
</script>

<style lang="less" scoped>
  .newTag{
    margin: 20px;
  }
</style>