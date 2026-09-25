import {createStore} from  "vuex";
const store = createStore({
    state:{
        user:null
    },
    mutations:{
        setUser(state,user){
            state.user = user
        }
    }
})
export default store