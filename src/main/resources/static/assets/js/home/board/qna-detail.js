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
            const data = {
                createdDateText: useFilters().YYYYMMDD(res.data.updatedAt || res.data.createdAt),
                ...res.data
            }
            var template = document.getElementById("qna-detail-body").innerHTML;
            var result = Mustache.render(template, data);
            document.getElementById("qna-detail-body").innerHTML = result;
            
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
                if(role === 'ADMIN'){
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
    replyBtn.style.display = 'block'
    replyBtn.addEventListener('click', function(){
        const replyContainer = document.getElementById('replyContainer')
        replyContainer.style.display = 'block'
    })
    

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
    

// 게시글 수정
async function updateBoard(qnaId) {
    await useAxios.put(`/api/v1/boards/${qnaId}/board`,
            {}
            ,(res)=> {
            // console.log('getBoardReply',res.data)
        },(err)=> {
            console.log('err',err)
            // document.getElementById("qna-detail-body").innerHTML = '<div class="detail-head">QnA 내용이 없습니다.</div>'
        })
}
// 게시글 삭제
async function deleteBoard(qnaId) {
    await useAxios.delete(`/api/v1/boards/${qnaId}/board`,
            {}
            ,(res)=> {
            // console.log('getBoardReply',res.data)
        },(err)=> {
            console.log('err',err)
            // document.getElementById("qna-detail-body").innerHTML = '<div class="detail-head">QnA 내용이 없습니다.</div>'
        })
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