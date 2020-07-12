<template>
  <div class="newCategory">
    <el-form ref="categoryForm" :model="categoryForm" :rules="rules" label-width="80px">
      <el-form-item label="类别名称" prop="name" >
        <el-input  v-model="categoryForm.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="createCategory">创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "NewCategory",
    data(){
      return{
        categoryForm:{
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
      createCategory(){
        this.$refs.categoryForm.validate(valid=>{
          if(!valid){
            this.$message({
              message:'表单错误',
              type:'warning'
            })
          }else{
            this.submitCategory()
          }
        })
      },
      async submitCategory(){
        let {data:res} = await postRequest('category/new',this.categoryForm);
        console.log(res);
        checkToken(res)
        this.$router.push({name:'Category'})
      }
    }
  }
</script>

<style lang="less" scoped>
  .newCategory{
    margin: 20px;
  }
</style>