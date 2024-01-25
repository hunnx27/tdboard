import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'
let dataArr = [];
$(function(){
    getEducations(1, 1000)
})

async function getEducations(pageNumber, size){
    await useAxios.get('/api/v1/educations',
        {
            page: pageNumber,
            size: size
        }
        ,(res)=> {
            if(res.data.paging.totalElements > 0){

            }
            if(res.data.contents){
                console.log(res.data.contents)
                const contents = res.data.contents;
                const now = new Date();  // 현재 날짜 및 시간을 가져옵니다.
                for(let i=0; i<contents.length; i++) {
                    const start = new Date(contents[i].startDate);
                    const end = new Date(contents[i].endDate);
                    const title = contents[i].name;
                    const url = `/contents/edu-detail/${contents[i].id}`
                    console.log("now:" + now)
                    console.log("start:" +start)
                    console.log("end:" +end)
                    console.log("url:" +url)

                    dataArr.push({
                        start: start,
                        end: end,
                        title: title,
                        url: url
                    })
                }
            }
            console.log(dataArr)
            calendarInit(dataArr);
        },(err)=> {
            console.log('err',err)
        })
}



// 캘린더 초기화
async function calendarInit(dataArr) {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        locale: 'ko',
        height: 'auto',
        navLinks: true,
        businessHours: true,
        editable: false,// 드래그 방지함
        selectable: true,
        buttonText: {
            today: "오늘",
            month: "월",
            week: "주",
            day: "일",
        },
        eventClick: function (req) {
            console.log('zz');
        },
        eventChange: function (req) {
            console.log('zz2');
        },
        eventDidMount: function (req) {
            console.log('ddd');
        },
        events: dataArr
    });
    calendar.render();
}
