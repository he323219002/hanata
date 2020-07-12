import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    esRes:''
  },
  mutations: {
    setESRes(state,payload){
      state.esRes = payload.esRes
    }
  },
  actions: {
  },
  modules: {
  }
})
