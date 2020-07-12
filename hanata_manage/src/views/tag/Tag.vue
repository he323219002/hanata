<template>
  <div class="tag">
    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <el-table
              :data=tag
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
    name: "Tag",
    created(){
      this.getTag();
    },
    data(){
      return {
        tag:[],
        search:'',
      }
    },
    methods:{
      async getTag(){
        let {data:res} = await postRequest("tag");
        checkToken(res);
        this.tag=res.data
      },

      handleNew(){
        this.$router.push({name:"NewTag"})
      },
      handleEdit(index, row) {
        console.log(row);
        let tagId = row.uid
        let tagName = row.name
        let payload = {
          'id':tagId,
          'name':tagName
        }
        this.$store.commit('setTagData',payload)

        this.$router.push({name:'UpdateTag'})
      },

      async handleDelete(index, row) {

        let {data:res} = await postRequest('tag/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },

      listArticle(id){
        this.$router.push({
          path:'/dairy',
          query:{'tid':id}
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
        let {data:res} = await postRequest("tag",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.tag = res.data;
        }
      }
    },
  }
</script>

<style scoped>

</style>