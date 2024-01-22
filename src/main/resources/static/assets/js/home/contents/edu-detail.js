import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(function(){
    const eduId = document.getElementById('eduId').value
    getEducationsApi(eduId)
 
    
})
async function getEducationsApi(id){
    await useAxios.get(`/api/v1/educations/${id}`,
        {}
        ,(res)=> {
           
        if(res.data){
            
            const startDate = new Date(res.data.startDate);
            const endDate = new Date(res.data.endDate);

            // 일수 계산
            const timeDifferenceInDays = Math.floor((endDate - startDate) / (1000 * 60 * 60 * 24));

            // 총 시간 계산
            const timeDifferenceInHours = Math.floor((endDate - startDate) / (1000 * 60 * 60));
            
            const data = {
                timeDifferenceInDays: timeDifferenceInDays,
                timeDifferenceInHours: timeDifferenceInHours,
                // createdDateText: useFilters().YYYYMMDD(data.updatedAt || data.createdAt),
                ...res.data,
            }
            
            var template = document.getElementById("galleryDetail").innerHTML;
            var result = Mustache.render(template, data);
            document.getElementById("galleryDetail").innerHTML = result;

            const applicationBtn = document.getElementById("applicationBtn")
            //신청 기간동안만 버튼 활성화
            applicationBtn.disabled = false

            applicationBtn.addEventListener("click", async () =>{
               
                handleApplication(id)
                
            })
        }
    },(err)=> {
        console.log('err',err)
    })

}

// 교육 신청
async function handleApplication(id){
    await useAxios.post('/api/v1/users/me/applications',
    {educationId: id}
        ,(res)=> {
        if(res.data){
            alert('신청되었습니다.')
        }else {
            alert('신청이 실패되었습니다.')
        }
    
    },(err)=> {
        alert(err.response.data.message)
    })
}