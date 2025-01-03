"""
OCI OpenSearch Service sample notebook.

Copyright (c) 2024 Oracle, Inc. All rights reserved. Licensed under the [Universal Permissive License (UPL) v 1.0](https://oss.oracle.com/licenses/upl/).
"""

from PIL import Image as PILImage
from IPython.display import display, Image
import os

def stack_images_horizontally(image_paths, output_path="stacked_image.png", width=None, height=None):
    """
    Stack multiple images horizontally and display the result.

    Parameters:
        image_paths (list of str): List of file paths to the images.
        output_path (str): Path to save the resulting stacked image.
        width (int, optional): Resize all images to this width (maintains aspect ratio).
        height (int, optional): Resize all images to this height (maintains aspect ratio).
    
    Returns:
        None
    """
    # Load all images
    images = []
    for path in image_paths:
        if not os.path.exists(path):
            print(f"File not found: {path}")
            continue
        img = PILImage.open(path)
        
        # Resize if width or height is provided
        if width or height:
            img = img.resize(
                (width if width else img.width, height if height else img.height),
                PILImage.LANCZOS  # Use LANCZOS for resizing
            )
        images.append(img)

    # Check if there are any valid images
    if not images:
        print("No valid images to stack.")
        return

    # Calculate total width and maximum height for the stacked image
    total_width = sum(img.width for img in images)
    max_height = max(img.height for img in images)

    # Create a blank canvas for the stacked image
    stacked_image = PILImage.new("RGB", (total_width, max_height), color=(255, 255, 255))

    # Paste images onto the canvas
    current_x = 0
    for img in images:
        stacked_image.paste(img, (current_x, 0))
        current_x += img.width

    # Save and display the stacked image
    stacked_image.save(output_path)
    # print(f"Stacked image saved to {output_path}")
    display(PILImage.open(output_path))

    
    
def show_image(image_path, description="Image", width=None, height=None):
    """
    Displays on image 

    Parameters:
        image_path (str): file path to the image.
        description (str): Image description.
        width (int, optional):Image display width
        height (int, optional): Image display Heigth
    
    Returns:
        None
    """
    print(description)
    if width or height:
        # Add resizing capabilities
        display(Image(filename=image_path, width=width, height=height))
    else:
        display(Image(filename=image_path))
