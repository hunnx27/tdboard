import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    getNoticeApi(1)
 
    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getNoticeApi(pageNumber)
     })
})
async function getNoticeApi(pageNumber){
    await useAxios.get('/api/v1/boards/type/notices',
        {page: 1}
        ,(res)=> {
        // console.log('res',res.data)
        if(res.data.totalElements > 0){
            res.data.content.map((data)=>{
                data.createdDateText = useFilters().YYYYMMDD(data.createdDate)
            })
            handleSetList(pageNumber, res.data)
        }else {
            paginationModule.setPage(1,[])
        }
        const tableHeadText = `Total ${res.data.totalElements}건 ${res.data.totalPages} 페이지`
        document.getElementById("tableHeadText").innerHTML = tableHeadText

    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("notice-table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("notice-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.totalElements)
}