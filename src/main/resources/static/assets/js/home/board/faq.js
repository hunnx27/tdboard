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
        // console.log('res',res.data)
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
    
    const deleteBtn = document.querySelectorAll('.deleteBtn')
    deleteBtn.forEach(element => {
        element.addEventListener("click",()=>{
            deleteApi(element.dataset.value)
        })
    });

    
    // paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}

async function deleteApi(id){
    if(confirm('글을 삭제하시겠습니까?')){
        await useAxios.delete(`/api/v1/boards/${id}`,
        {}
        ,(res)=> {
            alert('글이 삭제 되었습니다')
            // location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
    }
}