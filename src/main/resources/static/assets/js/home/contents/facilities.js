import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(function(){

    getFacilitiesApi(1)
 
})
async function getFacilitiesApi(pageNumber){
    await useAxios.get('/api/v1/facilities',
        {}
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
           
            handleSetList(pageNumber, res.data)
        }
    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("gallery").innerHTML = result;
    
}