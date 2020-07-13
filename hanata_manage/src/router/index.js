import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

  const routes = [
    {
      path:'/home',
      redirect: '/article'
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('../views/Home'),
      children: [
        {path:'/article',name:'Article',component: () => import('../views/article/Article')},
        {path:'/article/new',name:'NewArticle',component: () => import('../views/article/NewArticle')},
        {path:'/article/update',name:'UpdateArticle',component: () => import('../views/article/UpdateArticle')},
        {path:'/article/show',name:'ShowArticle',component: () => import('../views/article/ShowArticle')},

        {path:'/category',name:'Category',component:()=> import('../views/category/Category')},
        {path:'/category/new',name:'NewCategory',component:()=> import('../views/category/NewCategory')},
        {path:'/category/update',name:'UpdateCategory',component:()=> import('../views/category/UpdateCategory')},

        {path:'/label',name:'Label',component:()=> import('../views/label/Label')},
        {path:'/label/new',name:'NewLabel',component:()=> import('../views/label/NewLabel')},
        {path:'/label/update',name:'UpdateLabel',component:()=> import('../views/label/UpdateLabel')},

        {path:'/dairy',name:'Dairy',component:()=> import('../views/dairy/Dairy')},
        {path:'/dairy/new',name:'NewDairy',component:()=> import('../views/dairy/NewDairy')},
        {path:'/dairy/update',name:'UpdateDairy',component:()=> import('../views/dairy/UpdateDairy')},
        {path:'/dairy/show',name:'ShowDairy',component: () => import('../views/dairy/ShowDairy')},

        {path:'/tag',name:'_Tag',component:()=> import('../views/tag/Tag')},
        {path:'/tag/new',name:'NewTag',component:()=> import('../views/tag/NewTag')},
        {path:'/tag/update',name:'UpdateTag',component:()=> import('../views/tag/UpdateTag')},

        {path:'/record',name:'Record',component:()=>import('../views/Record')},

        {path:'/update/new',name:'NewUpdate',component:()=>import('../views/update/NewUpdate')},
        {path:'/update/',name:'Update',component:()=>import('../views/update/Update')},
        {path:'/update/update',name:'UpdateUpdate',component:()=>import('../views/update/UpdateUpdate')},

        {path:'/user',name:'User',component:()=>import('../views/user/User')}
      ]
    },
    {
      path:'/',
      redirect:'/login',
    },
    {
      path:'/login',
      name:'Login',
      component: () => import('../views/Login')
    },

]

const router = new VueRouter({
  base:'/manage/',
  mode:'history',
  routes
})

export default router
