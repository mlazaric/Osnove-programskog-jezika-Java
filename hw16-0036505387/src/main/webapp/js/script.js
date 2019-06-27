function loadTags() {
    $.ajax({
        url: 'rest/tag',
        dataType: 'json'
    }).done(createTagButtons);
}

function createTagButtons(tags) {
    let tagsDiv = $('#tags');

    for (let index = 0; index < tags.length; ++index) {
        let tag = tags[index];

        let button =
            $('<button />')
                .text(tag)
                .click(() => loadThumbnailsForTag(tag))
                .addClass('tag')
                .attr('data-id', tag);

        tagsDiv.append(button);
    }
}

function loadThumbnailsForTag(tag) {
    $.ajax({
        url: 'rest/tag/' + tag,
        dataType: 'json'
    }).done(createThumbnails);

    // Clear currently active
    $('button.tag.active').removeClass('active');

    // Set the new tag as active
    $('button.tag[data-id="' + tag + '"]').addClass('active');

    // Set the span's text to the current tag
    $('span#selected_tag').text(tag);
}

function createThumbnails(images) {
    let thumbnailsDiv = $('#thumbnails');

    // Clear any thumbnails we had before
    thumbnailsDiv.html('');

    // Show its parent if it is still hidden
    thumbnailsDiv.parent().removeClass('hidden');

    for (let index = 0; index < images.length; ++index) {
        let image = images[index];

        let thumbnail =
            $('<img />')
                .attr('src', 'thumbnails/' + image)
                .click(() => loadImage(image))
                .addClass('thumbnail');

        thumbnailsDiv.append(thumbnail);
    }
}

function loadImage(image) {
    $.ajax({
        url: 'rest/image/' + image,
        dataType: 'json'
    }).done(createDescription);

    // Show its parent if it is hidden
    $('img#image').parent().removeClass('hidden');

    // Clear image description
    $('p#image_description').text();

    $('img#image').attr('src', 'images/' + image);
}

function createDescription(imageData) {
    $('#image_description').text(imageData['description']);
    $('#image_tags').text(imageData['tags'].join(', '));
}

$(document).ready(loadTags);