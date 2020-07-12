<template>
  <div class="label">
    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <el-table
              :data=label
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
    name: "Label",
    created(){
      this.getAllLabels();
    },
    data(){
      return {
        label:[],
        search:'',
      }
    },
    methods:{

      handleNew(){
        this.$router.push({name:"NewLabel"})
      },

      handleEdit(index, row) {
        let labelId = row.uid
        let labelName = row.name
        let payload = {
          'id':labelId,
          'name':labelName
        }
        this.$store.commit('setLabelData',payload)

        this.$router.push({name:'UpdateLabel'})
      },

      async handleDelete(index, row) {

        let {data:res} = await postRequest('label/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },

      listArticle(id){
        this.$router.push({
          path:'/article',
          query:{'lid':id}
        })
      },


      async getAllLabels(){
        let {data:res} = await postRequest('label');
        checkToken(res)
        if(res.code===0){
          this.label=res.data
        }
      }


    },
    watch:{
      search: async function(newVal,oldVal){
        let pageParams = {
          'q':this.search,
        }
        let {data:res} = await postRequest("label",pageParams);
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