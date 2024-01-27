import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function () {
    const qnaId = document.getElementById("qnaId")
    if(qnaId?.value){
        getBoard(qnaId?.value)
        getBoardReply(qnaId?.value)
        upteBoardHit(qnaId?.value)
    }else {
        location.href="/contents/data"
    }

    
    
    
}
// qna 조회
async function getBoard(qnaId) {
    await useAxios.get(`/api/v1/boards/type/qnas/${qnaId}`,
            {}
            ,(res)=> {
            // console.log('res',res.data)
            document.getElementById("title").innerText = res.data.title
            document.getElementById("createdDateText").innerText = useFilters().YYYYMMDD(res.data.updatedAt || res.data.createdAt)
            document.getElementById("context").innerHTML = res.data.context

            // 파일첨부 리스트
            if(res.data?.files?.length > 0){
                var fileTemplate = document.getElementById("file-template").innerHTML;
                var fileResult = Mustache.render(fileTemplate, {files: res.data?.files});
                document.getElementById("file-list-body").innerHTML = fileResult;
            }
            
            const deleteBtn = document.getElementById('deleteBtn')
            if(deleteBtn){
                deleteBtn.addEventListener('click',()=>{
                    deleteBoardApi(qnaId)
                })
            }
            const userId = document.getElementById('userId').value
            if(`${userId}` === `${res.data.userId}`){
                deleteBtn.style.display = 'block'
                const modifyBtn = document.getElementById('modifyBtn')
                modifyBtn.style.display = 'block'
            }


        },(err)=> {
            console.log('err',err)
            document.getElementById("qna-detail-body").innerHTML = '<div class="detail-head">QnA 내용이 없습니다.</div>'
        })
}
// 댓글 조회
async function getBoardReply(qnaId) {
    await useAxios.get(`/api/v1/boards/${qnaId}/reply-board`,
            {}
            ,(res)=> {
           
            const role = document.getElementById('role').value
            if(res.data.contents.length > 0) {
                const data = {
                    replyText: res.data.contents[0].context,
                    replyCreatedDateText: useFilters().YYYYMMDD(res.data.contents[0].updatedAt || res.data.contents[0].createdAt),
                    ...res.data.contents[0]
                }
                const replyDetail = document.getElementById('replyDetailBody')
                replyDetail.style.display = 'block'
                var template = document.getElementById("reply-template").innerHTML;
                var result = Mustache.render(template, data);
                document.getElementById("replyDetailBody").innerHTML = result;

                const replyContainer = document.getElementById('replyContainer')
                replyContainer.style.display = 'block'
            }else {
                
                if(role === 'true'){
                    console.log('role',role)
                    initElementEvent(qnaId)
                }
            }
        },(err)=> {
            console.log('err',err)
        })
}
// 클릭이벤트
function initElementEvent(qnaId) {
    const replyForm = document.getElementById('replyForm')
    replyForm.style.display = 'block'

    const replyBtn = document.getElementById('replyBtn')
    
    replyBtn.addEventListener('click', function(){
        const replyContainer = document.getElementById('replyContainer')
        replyContainer.style.display = 'block'
    })
    replyBtn.style.display = 'block'

    const replySaveBtn = document.getElementById('replySaveBtn')
    replySaveBtn.style.display = 'block'
    replySaveBtn.addEventListener('click', () =>{
        createBoardReply(qnaId)
    })

    const replyTextarea = document.getElementById('replyTextarea')
    replyTextarea.addEventListener('input', () =>{
        if(replyTextarea.value){
            replySaveBtn.disabled = false
        }else {
            replySaveBtn.disabled = true
        }
    })
}

// 댓글 생성
async function createBoardReply(qnaId) {
    const replyTextarea = document.getElementById('replyTextarea')
    await useAxios.post(`/api/v1/boards/${qnaId}/reply-board`,
            {
                'boardType': 'QNA',
                'context': replyTextarea.value,
                'userId': ''
            }
            ,(res)=> {
            location.reload()
        },(err)=> {
            console.log('err',err)
            alert('오류가 발생하였습니다.')
        })
}
    


async function deleteBoardApi(qnaId){
    if(confirm('글을 삭제하시겠습니까?')){
        await useAxios.delete(`/api/v1/boards/${qnaId}`,
        {}
        ,(res)=> {
            alert('글이 삭제 되었습니다')
            location.href= "/contents/qna"
        },(err)=> {
            alert(err.response.data.message)
        })
    }
}

// 댓글 조회수 업데이트
async function upteBoardHit(qnaId) {
    await useAxios.post(`/api/v1/boards/${qnaId}/up-hit`,
            {}
            ,(res)=> {
        },(err)=> {
            console.log('err',err)
        })
}