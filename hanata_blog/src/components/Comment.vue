<template>
  <div class="comment" >

    <div class="comment-contain">
      <div class="comment-list" v-if="comment.list && comment.list.length>0">
        <div class="comment-item" v-for="(item,index) in comment.list" :key="index">
          <div class="comment-avatar">
            <div>
              <img :src="item.userAvatar" alt="">
            </div>
          </div>
          <div class="comment-right">
            <div class="comment-name">{{item.userName}}</div>
            <div class="comment-content">{{item.content}}</div>
            <div class="comment-reply">
              <div class="comment-reply-time">{{item.createTime}}</div>
              <el-button type="primary" style="transform: translate(0,-150%)" @click="reply(item.userName,item.uid,item.uid)">回复</el-button>
            </div>

            <div class="comment-children-list" v-if="item.appending && item.appending.length > 0">
              <div class="comment-children-item" v-for="(childItem,ind) in item.appending" :key="ind">
                <div class="comment-child-avatar">
                  <div>
                    <img :src="childItem.userAvatar" alt="">
                  </div>
                </div>
                <div class="comment-children-right">
                  <div class="comment-name">{{childItem.userName}}</div>
                  <div class="comment-content">{{childItem.content}}</div>
                  <div class="comment-reply">
                    <div class="comment-reply-time">{{childItem.createTime}}</div>
                    <el-button type="primary" style="transform: translate(0,-150%)" @click="reply(childItem.userName,childItem.fartherId,childItem.targetId)">回复</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="comment-none" v-else>暂无数据</div>
    </div>

    <div class="pagination" style="transform: scale(1.2)">
      <el-pagination
              style="height: 100%;text-align: center;margin: 20px"
              background
              :page-size="10"
              :pager-count="11"
              layout="prev, pager, next"
              :total="comment.total"
              @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>


  </div>
</template>

<script>
  import {postRequest} from "../utils/api";
  import {checkToken} from "../utils/utils";
  import {picBaseURL} from "../utils/api";

  export default {
    name: "Comment",
    data(){
      return{
        comment:'',
        picBaseURL:picBaseURL,
        total:'',
        kind:''
      }
    },
    props:['id'],
    methods:{
      async getCommentById(id,kind){
        let {data:res} = await postRequest('comment',{'articleId':id,'kind':kind});
        checkToken(res)
        this.comment=res.data
        this.kind=kind
        // this.comment.list.forEach((a)=>{
        //   let commentId = a.uid;
        //   postRequest('comment/append',{'commentId':commentId}).then((res)=>{
        //     // console.log(res);
        //     if (res.data.code===0){
        //       a.appending = res.data.data || []
        //     }
        //   })
        // })
      },
      async handleCurrentChange(val){
        let Pageparams = {
          "articleId":this.$route.query.id,
          "limit": val+'',
          "kind":this.kind 
        }
        let {data:res} = await postRequest("comment",Pageparams);
        this.comment = res.data;
        console.log(res);
      },
      reply(username,fid,targetId){
        this.$emit('commenter',username,fid,targetId)
      },
    }
  }
</script>

<style lang="less" scoped>
  .comment{
    width: 100%;
    height: auto;

    .comment-contain{
      width: 100%;
      height: auto;

      .comment-list{
        width: 100%;
        height: auto;

        .comment-item{
          with: 100%;
          height: auto;
          display: flex;
          flex-direction: row;

          .comment-avatar{
            width:100px;

            & > div{
              width: 60px;
              height: 60px;
              border-radius: 50%;
              overflow: hidden;
              margin: 0 auto;

              & > img{
                width:100%;
                height:100%;
              }

            }
          }

          .comment-right{
            width: calc(100% - 100px);

            .comment-children-list{
              width:100%;
              height: auto;

              .comment-children-item{
                width: 100%;
                height: auto;
                display: flex;
                flex-direction: row;

                .comment-child-avatar{
                  width:80px;

                  & > div{
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    overflow: hidden;
                    margin: 0 auto;

                    & > img{
                      width:100%;
                      height:100%;
                    }

                  }
                }

                .comment-children-right{
                  width: calc(100% - 80px);
                }

              }
            }
          }

          .comment-name{
            font-size: 18px;
            color: #ffffff;
          }

          .comment-content{
            width: calc(100% - 30px);
            height: auto;
            line-height: 25px;
            padding: 15px 15px;
            font-size: 16px;
            color: #ffffff;
          }

          .comment-reply{
            width: 100%;
            height: 45px;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;

            .comment-reply-time{
              font-size: 16px;
              color: #dddddd;
            }
          }

        }
      }
    }
  }

</style>