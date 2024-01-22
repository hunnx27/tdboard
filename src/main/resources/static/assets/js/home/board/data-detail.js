import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const dataId = document.getElementById("dataId")
    if(dataId?.value){
        await useAxios.get(`/api/v1/boards/type/datas/${dataId.value}`,
            {}
            ,(res)=> {
            // console.log('res',res.data)
            const data = {
                createdDateText: useFilters().YYYYMMDD(res.data.updatedAt || res.data.createdAt),
                ...res.data
            }
            var template = document.getElementById("dataㄴ-detail-body").innerHTML;
            var result = Mustache.render(template, data);
            document.getElementById("data-detail-body").innerHTML = result;
            
        },(err)=> {
            console.log('err',err)
            document.getElementById("data-detail-body").innerHTML = '<div class="detail-head">자료실 내용이 없습니다.</div>'
        })
    }else {
        location.href="/contents/data"
    }
    
})