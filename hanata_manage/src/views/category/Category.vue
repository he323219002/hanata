<template>
  <div class="category">
    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <el-table
              :data=category
              style="width: 100%"
      >
        <el-table-column
                label="名称"
                prop="name"
        >
        </el-table-column>

        <el-table-column
                label="文章数量"
        >
          <template slot-scope="scope">
            <p @click="listArticle(scope.row.uid)" style="cursor: pointer;">{{scope.row.count}}</p>
          </template>

        </el-table-column>

        <el-table-column
                label="日期"
                prop="createTime">
        </el-table-column>

        <el-table-column
                align="right">
          <template slot="header" slot-scope="scope">

            <el-input
                    v-model="search"
                    size="mini"
                    placeholder="搜索"/>
          </template>


          <template slot-scope="scope">
            <el-button
                    size="mini"
                    @click="handleEdit(scope.$index, scope.row)"
                    @click.stop.native>编辑
            </el-button>

            <el-popconfirm
                    title="确定删除吗？"
                    @onConfirm="handleDelete(scope.$index, scope.row)"
                    @click.stop.native
            >
              <el-button
                      slot="reference"
                      size="mini"
                      type="danger">删除
              </el-button>
            </el-popconfirm>
          </template>

        </el-table-column>

      </el-table>


    </template>
  </div>
</template>

<script>
  import {postRequest} from "../../utils/api";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "Category",
    created(){
      this.getCategory();
    },
    data(){
      return {
        category:[],
        search:'',
      }
    },
    methods:{
      async getCategory(){
        let {data:res} = await postRequest("category");
        checkToken(res);
        this.category=res.data
      },

      handleNew(){
        this.$router.push({name:"NewCategory"})
      },
      handleEdit(index, row) {
        let categoryId = row.uid
        let categoryName = row.name
        let payload = {
          'id':categoryId,
          'name':categoryName
        }
        this.$store.commit('setCategoryData',payload)

        this.$router.push({name:'UpdateCategory'})
      },

      async handleDelete(index, row) {

        let {data:res} = await postRequest('category/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },

      listArticle(id){
        this.$router.push({
          path:'/article',
          query:{'cid':id}
        })
      }

    },
    watch:{
      search: async function(newVal,oldVal){
        console.log(newVal);
        console.log(oldVal);
        let pageParams = {
          'q':this.search,
        }
        let {data:res} = await postRequest("category",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.category = res.data;
        }
      }
    },
  }
</script>

<style scoped>

</style>