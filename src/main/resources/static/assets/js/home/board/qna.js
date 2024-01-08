import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    getQnaApi(1)
 
    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getQnaApi(pageNumber)
     })
})
async function getQnaApi(pageNumber){
    await useAxios.get('/api/v1/boards/type/qnas',
        {page: pageNumber-1}
        ,(res)=> {
        console.log('res',res.data)
        if(res.data.paging.totalElements > 0){
            res.data.contents.map((data)=>{
                data.createdDateText = useFilters().YYYYMMDD(data.createdDate)
            })
            handleSetList(pageNumber, res.data)
        }else {
            paginationModule.setPage(1,0,0)
        }
        const tableHeadText = `Total ${res.data.paging.totalElements}건 ${res.data.paging.totalPages} 페이지`
        document.getElementById("tableHeadText").innerHTML = tableHeadText

    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("qna-table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("qna-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}