webpackJsonp([3],{X31z:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s={data:()=>({tableData:[],delVisible:!1,key_title:"",key_content:"",key_username:"",one:{},page_curr:1,page_total:10,page_size:10}),created(){},computed:{},methods:{search(){""==this.key_username&&""==this.key_title&&""==this.key_content?this.$message.warning("关键字不能全部为空！"):this.pageChange(1)},pageChange(e){var t=this;t.$axios({method:"post",url:"/forum/searchPostByUsernameAndContentAndTitle?pageNum="+e+"&pageSize=10",data:{title:t.key_title,username:t.key_username,content:t.key_content}}).then(e=>{console.log(e.data.payload),t.tableData=new Array,t.tableData=t.tableData.concat(e.data.payload.list),t.page_size=e.data.payload.pageSize,t.page_total=e.data.payload.total}).catch(e=>{console.warn("catch :"),console.log(e),t.$message.error("错误")})},handleDelete(e,t){this.one=t,this.delVisible=!0},deleteRow(){var e=this;e.$axios({method:"get",url:"/forum/deletePost/"+e.one.id}).then(t=>{const a=e.tableData.indexOf(e.one);e.tableData.splice(a,1),this.$message.success("删除成功")}).catch(t=>{console.warn("catch :"),console.log(t),e.$message.error("错误")}),this.delVisible=!1},handleRecommend(e,t){this.recommend(t)},recommend(e){var t=this;t.$axios({method:"get",url:"/forum/recommendPost/"+e.id}).then(a=>{t.tableData.indexOf(e);1==a.data.code?this.$message.success("推荐成功"):0==a.data.code?this.$message.error("每个版块推荐帖个数不能超过5"):2==a.data.code&&this.$message.error("不能重复推荐帖子")}).catch(e=>{console.warn("catch :"),console.log(e),t.$message.error("错误")})},handleTop(e,t){this.top(t)},top(e){var t=this;t.$axios({method:"get",url:"/forum/addStickyPost/"+e.id}).then(a=>{console.log(a);t.tableData.indexOf(e);1==a.data.code?this.$message.success("设置置顶成功"):0==a.data.code?this.$message.error("每个版块置顶帖个数不能超过5"):2==a.data.code&&this.$message.error("不能重复置顶帖子")}).catch(e=>{console.warn("catch :"),console.log(e),t.$message.error("错误")})},handleCurrentChange(e){this.page_curr=e,console.log(this.page_curr),this.pageChange(this.page_curr)}}},l={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"table"},[a("div",{staticClass:"crumbs"},[a("el-breadcrumb",{attrs:{separator:"/"}},[a("el-breadcrumb-item",[a("i",{staticClass:"el-icon-tickets"}),e._v(" 帖子管理")])],1)],1),e._v(" "),a("div",{staticClass:"container"},[a("div",{staticClass:"handle-box"},[a("el-input",{staticClass:"handle-input mr10",attrs:{placeholder:"作者名称"},model:{value:e.key_username,callback:function(t){e.key_username=t},expression:"key_username"}}),e._v(" "),a("el-input",{staticClass:"handle-input mr10",attrs:{placeholder:"标题名称"},model:{value:e.key_title,callback:function(t){e.key_title=t},expression:"key_title"}}),e._v(" "),a("el-input",{staticClass:"handle-input mr10",attrs:{placeholder:"内容关键字"},model:{value:e.key_content,callback:function(t){e.key_content=t},expression:"key_content"}}),e._v(" "),a("el-button",{attrs:{type:"primary",icon:"search"},on:{click:function(t){e.search()}}},[e._v("搜索")])],1),e._v(" "),a("el-table",{ref:"multipleTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,border:""}},[a("el-table-column",{attrs:{prop:"username",label:"作者",sortable:"",width:"150"}}),e._v(" "),a("el-table-column",{attrs:{prop:"title",label:"标题",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{prop:"content",label:"内容"}}),e._v(" "),a("el-table-column",{attrs:{prop:"time",label:"时间"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作",width:"300"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{size:"small",type:"success"},on:{click:function(a){e.handleRecommend(t.$index,t.row)}}},[e._v("\n                        推荐\n                    ")]),e._v(" "),a("el-button",{attrs:{size:"small",type:"primary"},on:{click:function(a){e.handleTop(t.$index,t.row)}}},[e._v("\n                        置顶\n                    ")]),e._v(" "),a("el-button",{attrs:{size:"small",type:"danger"},on:{click:function(a){e.handleDelete(t.$index,t.row)}}},[e._v("\n                        删除\n                    ")])]}}])})],1),e._v(" "),a("div",{staticClass:"pagination"},[a("el-pagination",{attrs:{"current-page":e.page_curr,"page-size":e.page_size,layout:"prev, pager, next",total:e.page_total},on:{"current-change":e.handleCurrentChange}})],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"提示",visible:e.delVisible,width:"300px",center:""},on:{"update:visible":function(t){e.delVisible=t}}},[a("div",{staticClass:"del-dialog-cnt"},[e._v("删除不可恢复，是否确定删除？")]),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.delVisible=!1}}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:e.deleteRow}},[e._v("确 定")])],1)])],1)},staticRenderFns:[]};var n=a("vSla")(s,l,!1,function(e){a("icmk")},"data-v-7e1cb368",null);t.default=n.exports},icmk:function(e,t){}});