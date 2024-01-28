import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    getFacilitiesApi(1)

    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getFacilitiesApi(pageNumber)
     })
 
})
async function getFacilitiesApi(pageNumber){
    await useAxios.get('/api/v1/facilities',
        {
            page: pageNumber
        }
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

    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)

    
}