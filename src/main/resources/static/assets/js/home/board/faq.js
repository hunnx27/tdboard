import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(function(){

    getFaqApi(1)
 
})
async function getFaqApi(pageNumber){
    await useAxios.get('/api/v1/boards/type/faqs',
        {page: pageNumber}
        ,(res)=> {
        console.log('res',res.data)
        if(res.data.paging.totalElements > 0){
            res.data.contents.map((data)=>{
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
            })
            handleSetList(pageNumber, res.data)
        }else {
            paginationModule.setPage(1,1,0)
        }
        // const tableHeadText = `Total ${res.data.paging.totalElements}건 ${res.data.paging.totalPages} 페이지`
        // document.getElementById("tableHeadText").innerHTML = tableHeadText

    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("faq-table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("faq-body").innerHTML = result;
    
    // paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}