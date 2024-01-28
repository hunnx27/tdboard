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

    // "option" 클래스를 가진 버튼 요소들을 선택
    const optionButtons = document.querySelectorAll('.option');

    // 각 버튼에 클릭 이벤트 리스너 추가
    optionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 클릭된 버튼의 data-option 값을 가져와서 selectJoinPathField의 data-value에 설정
            const dataOptionValue = button.dataset.option;
            getBoardApi(1,dataOptionValue)
        });
    });
    
})
async function getBoardApi(pageNumber){
    await useAxios.get('/api/v1/bookings',
        {
            page: pageNumber,
            bookingType: 'EQUIPMENT'
        }
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{

                data.notApproval = data.approvalYn === 'N' && !data.approvalAt ? true : false // 승인대기
                data.approval = data.approvalYn === 'Y' && data.approvalAt ? true : false //승인완료
                data.cancelApplication = data.approvalYn === 'N' && data.approvalAt ? true : false //취소완료

                data.startAtText = useFilters().YYYYMMDD(data.startAt)
                data.endAtText = useFilters().YYYYMMDD(data.endAt)
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
                data.no = firstPostNumber
                firstPostNumber--;
            })
            handleSetList(pageNumber, res.data)
        }else {
            handleSetList(pageNumber, res.data)
            paginationModule.setPage(1,1,0)
        }
        const tableHeadText = `Total ${res.data.paging.totalElements}건 ${res.data.paging.totalPages} 페이지`
        document.getElementById("tableHeadText").innerHTML = tableHeadText

        const cancelBtn = document.querySelectorAll('.cancelBtn')
        cancelBtn.forEach(function (btns, idx) {
            if(btns){
                btns.addEventListener('click', function(e) {
                    // 클릭한 버튼의 data-value 속성 값 가져오기
                    const dataValue = this.getAttribute('data-value');
                    
                    handleCancel(dataValue);
                });
            }
        })

        const confirmBtn = document.querySelectorAll('.confirmBtn')
        confirmBtn.forEach(function (btns, idx) {
            if(btns){
                btns.addEventListener('click', function(e) {
                    // 클릭한 버튼의 data-value 속성 값 가져오기
                    const dataValue = this.getAttribute('data-value');
                    handleConfirm(dataValue)
                });
            }
        })
        
        
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

async function handleConfirm(id){
    if(confirm('승인 처리 하시겠습니까?')){
        await useAxios.post(`/api/v1/bookings/${id}/approval`,
        {}
            ,(res)=> {
                // console.log(res.data)
                alert('승인 처리 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
    }
    
}


async function handleCancel(id){
    if(confirm('취소 하시겠습니까?')){
        await useAxios.post(`/api/v1/bookings/${id}/cancel`,
        {}
            ,(res)=> {
                // console.log(res.data)
                alert('취소 처리 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
    }
    
}