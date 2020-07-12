<template>

  <div class="table">

    <template>
      <el-button type="primary" style="float: left;margin: 2px 2px" @click="handleNew">新增</el-button>

      <div class="select">
        <el-select v-model="state" placeholder="请选择">
          <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
          </el-option>
        </el-select>
      </div>

      <el-table
              :data=dairy.list
              style="width: 100%"
              @row-click="getDairyId"
      >
        <el-table-column
                label="标题"
                prop="title"
        >
        </el-table-column>
        <el-table-column
                label="点赞"
                prop="likeCount">
        </el-table-column>
        <el-table-column
                label="评论数"
                prop="commentCount">
        </el-table-column>
        <el-table-column
                label="点击数"
                prop="viewCount">
        </el-table-column>
        <el-table-column label="是否发送">
          <template slot-scope="scope">
            <p v-if="scope.row.state==='1'">已发送</p>
            <p v-else>草稿</p>
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
                    <!--@click="handleDelete(scope.$index, scope.row)">删除-->
            </el-button>
            </el-popconfirm>
          </template>

        </el-table-column>

      </el-table>

      <div class="block">
        <span class="demonstration"></span>
        <el-pagination
                layout="prev, pager, next"
                :total=dairy.total
                @current-change="handleCurrentChange"
        >
        </el-pagination>

      </div>

    </template>
  </div>

</template>
<script>
  import {postRequest} from "../../utils/api"
  import {AsyncComponentPromise as resolve} from "vue";
  import {checkToken} from "../../utils/utils";

  export default {
    name: "Dairy",
    created() {
      this.tid = this.$route.query.tid
      this.getDairy();
    },
    data() {
      return {
        dairy: [],
        search:'',
        tid:'',
        state:'',
        options:[
          {value:'', label:'全部'},
          {value:'1',label:'已发送'},
          {value:'0',label:'草稿'}
        ]
      }
    },
    methods: {
      async getDairy() {
        let {data: res} = await postRequest("dairy",{"admin":"1",'tid':this.tid,'q':this.search});

        checkToken(res)
        this.dairy = res.data;

      },
      handleNew(){
        this.$router.push({name:"NewDairy"})
      },
      handleEdit(index, row) {
        let dairyId = row.uid
        let payload = {
          'id':dairyId
        }
        this.$store.commit('setDairyData',payload)
        this.$router.push({name:'UpdateDairy'})
      },


      getDairyId(row,column,event){
        let DairyId = row.uid
        let payload = {
          'id':DairyId
        }
        this.$store.commit('setDairyData',payload)
        this.$router.push({name:'ShowDairy'})
      },

      async handleDelete(index, row) {

        let {data:res} = await postRequest('dairy/del',{'id':row.uid});
        if (res.code===0){
          this.$router.go(0)
        } else {
          console.log(res.data());
        }
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "limit": val+'',
          "admin": "1",
          'q':this.search,
          "tid": this.tid,
        }
        let {data:res} = await postRequest("dairy",Pageparams);
        if (res.code === 0) {
          console.log(res.data);
          this.dairy = res.data;
          // return []

        }
      }
    },
    watch:{
      search: async function(newVal,oldVal){

        let pageParams = {
          "admin": "1",
          'q':this.search,
          "tid": this.tid,
          "state":this.state
        }
        let {data:res} = await postRequest("dairy",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.dairy = res.data;
        }

      },
      state: async function(newV,oldV){
        let pageParams = {
          "admin": "1",
          'q':this.search,
          "tid": this.tid,
          "state":this.state
        }
        let {data:res} = await postRequest("dairy",pageParams);
        if (res.code === 0) {
          console.log(res.data);
          this.dairy = res.data;
        }
      }
    },

  }
</script>

<style lang="less" scoped>
  .select{
    float: left;
    margin: 2px;
  }
</style>