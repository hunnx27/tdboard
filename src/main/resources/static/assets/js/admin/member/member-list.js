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
async function getBoardApi(pageNumber, role){
    await useAxios.get('/api/v1/members',
        {
            page: pageNumber,
            role: role || undefined,
        }
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
                if(data.role === 'ROLE_USER'){
                    data.roleText = '회원'
                }else if(data.role === 'ROLE_ADMIN') {
                    data.roleText = '관리자'
                }else if(data.role === 'ROLE_PROFESSOR'){
                    data.roleText = '교수'
                }else {
                    data.roleText = data.role
                }
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

        const memberDeleteBtn = document.querySelectorAll('.memberDeleteBtn')
        memberDeleteBtn.forEach(function (btns, idx) {
            if(btns){
                btns.addEventListener('click', function(e) {
                    // 클릭한 버튼의 data-value 속성 값 가져오기
                    const dataValue = this.getAttribute('data-value');
                    // 회원 탈퇴 호출
                    handleMemberDelete(dataValue);
                });
            }
        })

        const memberModifyBtn = document.querySelectorAll('.memberModifyBtn')
        memberModifyBtn.forEach(function (btns, idx) {
            if(btns){
                btns.addEventListener('click', function(e) {
                    // 클릭한 버튼의 data-value 속성 값 가져오기
                    const dataValue = this.getAttribute('data-value');
                    // 회원 탈퇴 호출
                    location.href= `/admin/member-detail/${dataValue}`
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

// 회원 탈퇴
async function handleMemberDelete(id){
    if(confirm('탈퇴처리 하시겠습니까?')){
        await useAxios.delete(`/api/v1/members/${id}`,
        {}
            ,(res)=> {
                console.log(res.data)
                alert('탈퇴 처리 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
    }
    
}