import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const qnaId = document.getElementById("qnaId")
    if(qnaId?.value){
        await useAxios.get(`/api/v1/boards/type/qnas/${qnaId.value}`,
            {}
            ,(res)=> {
            console.log('res',res.data)
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
    }else {
        location.href="/contents/data"
    }
    
})