<template>
  <div class="newCategory">
    <el-form ref="categoryForm" :model="categoryForm" :rules="rules" label-width="80px">
      <el-form-item label="文章标题" prop="name" >
        <el-input v-model="categoryForm.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="updateCategory">更新</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    created(){
      this.getCategory()
    },
    name: "UpdateCategory",
    data(){
      return{
        categoryForm:{
          id:this.$store.state.categoryId,
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
      updateCategory(){
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
        let {data:res} = await postRequest('category/update',this.categoryForm);
        checkToken(res)

        if (res.code!==0){
          this.$message({
            message:res.msg,
            type:'warning'
          })
          return
        }
        this.$router.push({name:'Category'})
      },
      getCategory(){
        this.categoryForm.name=this.$store.state.categoryName
      }
    }
  }
</script>

<style lang="less" scoped>
  .newCategory{
    margin: 20px;
  }
</style>