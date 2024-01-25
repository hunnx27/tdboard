import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    getBoardApi(1)
 
    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getBoardApi(pageNumber)
     })
})
async function getBoardApi(pageNumber){
    await useAxios.get('/api/v1/users/me/applications',
        {page: pageNumber}
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
                data.notApproval = data.approvalYn === 'N' && !data.approvalAt ? true : false // 승인대기
                data.approval = data.approvalYn === 'Y' && data.approvalAt ? true : false //승인완료
                data.cancelApplication = data.approvalYn === 'N' && data.approvalAt ? true : false //취소완료
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt, 'YYYY-MM-DD HH:mm:ss')
                data.no = firstPostNumber
                firstPostNumber--;
            })
            handleSetList(pageNumber, res.data)
        }else {
            paginationModule.setPage(1,1,0)
        }
        const tableHeadText = `Total ${res.data.paging.totalElements}건 ${res.data.paging.totalPages} 페이지`
        document.getElementById("tableHeadText").innerHTML = tableHeadText



        const applicationCancelBtn = document.querySelector('.applicationCancelBtn')
        if(applicationCancelBtn){
            applicationCancelBtn.addEventListener('click', function() {
                // 클릭한 버튼의 data-value 속성 값 가져오기
                const dataValue = this.getAttribute('data-value');
        
                // 취소 함수 호출
                handleApplicationCancel(dataValue);
            });
        }
    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("list-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}

// 신청 취소
async function handleApplicationCancel(id){
    await useAxios.delete(`/api/v1/users/me/applications/${id}`,
    {}
        ,(res)=> {
            // console.log(res.data)
            alert('취소 되었습니다.')
            location.reload()
    },(err)=> {
        alert(err.response.data.message)
    })
}