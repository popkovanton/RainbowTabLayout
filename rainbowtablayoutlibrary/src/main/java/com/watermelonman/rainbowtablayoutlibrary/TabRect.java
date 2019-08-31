package com.watermelonman.rainbowtablayoutlibrary;

import android.graphics.Path;

class TabRect {

    private TabRect() {
    }

    private Path roundedRect(
            float left, float top, float right, float bottom, float cornerRadius,
            boolean tlCorner, boolean trCorner, boolean brCorner, boolean blCorner
    ) {
        Path path = new Path();
        if (cornerRadius < 0) cornerRadius = 0;
        if (cornerRadius < 0) cornerRadius = 0;
        float width = right - left;
        float height = bottom - top;
        if (cornerRadius > width / 2) cornerRadius = width / 2;
        if (cornerRadius > height / 2) cornerRadius = height / 2;
        float widthMinusCorners = (width - (2 * cornerRadius));
        float heightMinusCorners = (height - (2 * cornerRadius));

        path.moveTo(right, top + cornerRadius);
        if (trCorner)
            path.rQuadTo(0, -cornerRadius, -cornerRadius, -cornerRadius);//top-right corner
        else {
            path.rLineTo(0, -cornerRadius);
            path.rLineTo(-cornerRadius, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tlCorner)
            path.rQuadTo(-cornerRadius, 0, -cornerRadius, cornerRadius); //top-left corner
        else {
            path.rLineTo(-cornerRadius, 0);
            path.rLineTo(0, cornerRadius);
        }
        path.rLineTo(0, heightMinusCorners);

        if (blCorner)
            path.rQuadTo(0, cornerRadius, cornerRadius, cornerRadius);//bottom-left corner
        else {
            path.rLineTo(0, cornerRadius);
            path.rLineTo(cornerRadius, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (brCorner)
            path.rQuadTo(cornerRadius, 0, cornerRadius, -cornerRadius); //bottom-right corner
        else {
            path.rLineTo(cornerRadius, 0);
            path.rLineTo(0, -cornerRadius);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    /**
     * @return build TabRect instance
     */
    public static class Builder {
        private float left;
        private float top = 0;
        private float right;
        private float bottom;
        private float cornerRadius = 0;
        private boolean tlCorner = true;
        private boolean trCorner = true;
        private boolean brCorner = true;
        private boolean blCorner = true;

        public Builder setLeft(float left) {
            this.left = left;
            return this;
        }

        public Builder setTop(float top) {
            this.top = top;
            return this;
        }

        public Builder setRight(float right) {
            this.right = right;
            return this;
        }

        public Builder setBottom(float bottom) {
            this.bottom = bottom;
            return this;
        }

        public Builder setCornerRadius(float cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public Builder isTLCorner(boolean tlCorner) {
            this.tlCorner = tlCorner;
            return this;
        }

        public Builder isTRCorner(boolean trCorner) {
            this.trCorner = trCorner;
            return this;
        }

        public Builder isBRCorner(boolean brCorner) {
            this.brCorner = brCorner;
            return this;
        }

        public Builder isBLCorner(boolean blCorner) {
            this.blCorner = blCorner;
            return this;
        }

        public Path create() {
            return new TabRect().roundedRect(left, top, right,
                    bottom, cornerRadius, tlCorner, trCorner, brCorner, blCorner);
        }
    }
}
