webpackJsonp([0],{MpTN:function(e,t,l){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=new(l("MVMM").default),n={data:()=>({collapse:!1,fullscreen:!1,name:"linxin",message:2}),computed:{username(){let e=localStorage.getItem("ms_username");return e||this.name}},methods:{handleCommand(e){"loginout"==e&&(localStorage.removeItem("ms_username"),this.LogOut())},collapseChage(){this.collapse=!this.collapse,s.$emit("collapse",this.collapse)},handleFullScreen(){let e=document.documentElement;this.fullscreen?document.exitFullscreen?document.exitFullscreen():document.webkitCancelFullScreen?document.webkitCancelFullScreen():document.mozCancelFullScreen?document.mozCancelFullScreen():document.msExitFullscreen&&document.msExitFullscreen():e.requestFullscreen?e.requestFullscreen():e.webkitRequestFullScreen?e.webkitRequestFullScreen():e.mozRequestFullScreen?e.mozRequestFullScreen():e.msRequestFullscreen&&e.msRequestFullscreen(),this.fullscreen=!this.fullscreen},LogOut(){var e=this;e.$axios({method:"get",url:"/test/logout"}).then(t=>{e.$message.success("注销成功～"),setTimeout(e.toBack,2e3)}).catch(t=>{console.warn("catch :"),console.log(t),e.$message.error("错误")})},toBack(){this.$router.push("/login")}}},o={render:function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",{staticClass:"header"},[l("div",{staticClass:"collapse-btn",on:{click:e.collapseChage}},[l("i",{staticClass:"el-icon-menu"})]),e._v(" "),l("div",{staticClass:"logo"},[e._v("程序员社区后台管理系统")]),e._v(" "),l("div",{staticClass:"header-right"},[l("div",{staticClass:"header-user-con"},[l("div",{staticClass:"btn-fullscreen",on:{click:e.handleFullScreen}},[l("el-tooltip",{attrs:{effect:"dark",content:e.fullscreen?"取消全屏":"全屏",placement:"bottom"}},[l("i",{staticClass:"el-icon-rank"})])],1),e._v(" "),l("el-dropdown",{staticClass:"user-name",attrs:{trigger:"click"},on:{command:e.handleCommand}},[l("span",{staticClass:"el-dropdown-link"},[e._v("\n                    管理员菜单 "),l("i",{staticClass:"el-icon-caret-bottom"})]),e._v(" "),l("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[l("el-dropdown-item",{attrs:{command:"loginout"},on:{click:function(t){e.LogOut()}}},[e._v("退出登录")])],1)],1)],1)])])},staticRenderFns:[]};var a=l("vSla")(n,o,!1,function(e){l("yZAo")},"data-v-ab8330ea",null).exports,c={data:()=>({collapse:!1,items:[{icon:"el-icon-tickets",index:"4",title:"论坛管理",subs:[{index:"forum-recommend",title:"推荐管理"},{index:"forum-top",title:"置顶管理"},{index:"forum-post",title:"帖子管理"},{index:"forum-reply",title:"回复管理"}]},{icon:"el-icon-document",index:"5",title:"匿名区管理",subs:[{index:"anony-post",title:"匿名帖子管理"},{index:"anony-reply",title:"匿名回复管理"}]},{icon:"el-icon-message",index:"message",title:"私信管理"}]}),computed:{onRoutes(){return this.$route.path.replace("/","")}},created(){s.$on("collapse",e=>{this.collapse=e})}},i={render:function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",{staticClass:"sidebar"},[l("el-menu",{staticClass:"sidebar-el-menu",attrs:{"default-active":e.onRoutes,collapse:e.collapse,"background-color":"#324157","text-color":"#bfcbd9","active-text-color":"#20a0ff","unique-opened":"",router:""}},[e._l(e.items,function(t){return[t.subs?[l("el-submenu",{key:t.index,attrs:{index:t.index}},[l("template",{slot:"title"},[l("i",{class:t.icon}),l("span",{attrs:{slot:"title"},slot:"title"},[e._v(e._s(t.title))])]),e._v(" "),e._l(t.subs,function(t,s){return l("el-menu-item",{key:s,attrs:{index:t.index}},[e._v("\n                        "+e._s(t.title)+"\n                    ")])})],2)]:[l("el-menu-item",{key:t.index,attrs:{index:t.index}},[l("i",{class:t.icon}),l("span",{attrs:{slot:"title"},slot:"title"},[e._v(e._s(t.title))])])]]})],2)],1)},staticRenderFns:[]};var r={data:()=>({collapse:!1}),components:{vHead:a,vSidebar:l("vSla")(c,i,!1,function(e){l("vn9J")},"data-v-757cde1a",null).exports},created(){s.$on("collapse",e=>{this.collapse=e})}},u={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"wrapper"},[t("v-head"),this._v(" "),t("v-sidebar"),this._v(" "),t("div",{staticClass:"content-box",class:{"content-collapse":this.collapse}},[t("div",{staticClass:"content"},[t("transition",{attrs:{name:"move",mode:"out-in"}},[t("keep-alive",[t("router-view")],1)],1)],1)])],1)},staticRenderFns:[]},d=l("vSla")(r,u,!1,null,null,null);t.default=d.exports},vn9J:function(e,t){},yZAo:function(e,t){}});