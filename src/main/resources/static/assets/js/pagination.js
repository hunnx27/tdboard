
export default function () {
    const postsPerPage = 10;
    let totalPages = 0;
    let currentPage = 1;
    // 훅 함수
    let clickPageNumberHandler;
    // 페이지를 표시할 요소 선택
    const paginationElement = document.getElementById('pagination');
    const contentElement = document.getElementById('content');
   // 페이지를 클릭할 때마다 해당 페이지의 게시물 표시
   paginationElement.addEventListener('click', function (event) {
        if (event.target.tagName === 'A') {
            const pageNumber = parseInt(event.target.dataset.page || event.target.textContent);
            // showPosts(pageNumber);
            handleClickPageNumber(pageNumber)
        }
    });
    return {
        currentPage,
        showPosts(pageNumber)  {
            const startIndex = (pageNumber - 1) * postsPerPage;
            const endIndex = startIndex + postsPerPage;
            const currentPosts = Array.from({ length: postsPerPage }, (_, index) => ({
                id: startIndex + index + 1,
                content: `게시물 ${startIndex + index + 1}`,
            }));


            // 페이지 네이션 업데이트
            const template = document.getElementById('pagination-template').innerHTML;
            const data = {
                hasPrevious: currentPage > 1,
                hasNext: currentPage < totalPages,
                previous: currentPage - 1,
                next: currentPage + 1,
                pages: Array.from({ length: totalPages }, (_, index) => index + 1),
                isActive: function () {
                    return this === currentPage;
                },
            };
            const rendered = Mustache.render(template, data);
            paginationElement.innerHTML = rendered;
        },
        setPage(page, totalPosts) {
            // 전체 페이지 수 계산
            totalPages = Math.ceil(totalPosts / postsPerPage);
            currentPage = page;
            
            // 페이지 로딩 시 첫 페이지를 표시
            this.showPosts(currentPage);

            
        },
        // 클릭 이벤트 핸들러를 받는 함수
        setClickPageNumberHandler(handler) {
            clickPageNumberHandler = handler;
        }
        
    };
    function handleClickPageNumber(pageNumber){
        // 추가적인 동작 수행
        if (clickPageNumberHandler) {
            clickPageNumberHandler(pageNumber);
        }
    }
}