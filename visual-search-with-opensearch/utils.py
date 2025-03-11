import os
from torchvision import transforms, datasets
from torch.utils.data import Dataset
from PIL import Image, UnidentifiedImageError

# Optimized dataset class with Image ID lookup
class LocalImageDataset(Dataset):
    def __init__(self, root_dir, transform=None):
        self.root_dir = root_dir
        self.transform = transform
        self.classes = sorted([d for d in os.listdir(root_dir) if os.path.isdir(os.path.join(root_dir, d))])
        self.class_to_idx = {cls_name: idx for idx, cls_name in enumerate(self.classes)}

        # Collect all image paths, labels, and filenames
        self.image_paths = []
        self.labels = []
        self.image_ids = {}  # Dict to map image_id (filename) to index

        for cls_name in self.classes:
            cls_folder = os.path.join(root_dir, cls_name)
            for img_file in os.listdir(cls_folder):
                if img_file.endswith(("jpg", "jpeg", "png","webb")):
                    img_path = os.path.join(cls_folder, img_file)
                    self.image_paths.append(img_path)
                    self.labels.append(self.class_to_idx[cls_name])
                    self.image_ids[img_file] = len(self.image_paths) - 1  # Map filename to index

    def __len__(self):
        return len(self.image_paths)

    def __getitem__(self, idx):
        if isinstance(idx, str):  # If queried by image_id (filename)
            idx = self.image_ids.get(idx, None)
            if idx is None:
                raise KeyError(f"Image ID '{idx}' not found in dataset.")

        img_path = self.image_paths[idx]
        label = self.labels[idx]
        image_id = os.path.basename(img_path)  # Extract filename
        image=None
        try:
            image = Image.open(img_path).convert("RGB")
        except UnidentifiedImageError:
            print(f"Skipping corrupt/invalid image: {img_path}")
            return None, label

        if self.transform:
            image = self.transform(image)


        return image, label

