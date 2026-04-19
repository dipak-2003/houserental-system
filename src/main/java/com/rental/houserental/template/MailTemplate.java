package com.rental.houserental.template;

public class MailTemplate {

    public static String buildApprovalTemplate(String ownerName, String propertyTitle) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .header { background: #6366f1; color: white; padding: 30px; text-align: center; font-size: 24px; font-weight: bold; }
        .content { padding: 30px; color: #334155; line-height: 1.6; }
        .status-box { background: #f0fdf4; padding: 20px; border-radius: 8px; border-left: 4px solid #22c55e; margin: 20px 0; }
        .property-info { background: #f8fafc; padding: 15px; border-radius: 8px; border: 1px solid #e2e8f0; margin-top: 10px; }
        .badge { display: inline-block; padding: 4px 12px; background: #22c55e; color: white; border-radius: 20px; font-size: 11px; font-weight: bold; text-transform: uppercase; margin-top: 8px; }
        .footer { text-align: center; font-size: 12px; color: #94a3b8; padding: 25px; background: #f8fafc; border-top: 1px solid #e2e8f0; }
        strong { color: #1e293b; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">RentalHub</div>
        <div class="content">
            <h2 style="margin-top:0;">Hello %s,</h2>
            <div class="status-box">
                <strong style="color: #166534;">Congratulations!</strong> Your property listing has been approved and is now live.
            </div>
            <div class="property-info">
                <strong>Property:</strong> %s <br/>
                <span class="badge">Live & Approved</span>
            </div>
            <p>Tenants can now view and book your property. We'll notify you as soon as you receive inquiries.</p>
        </div>
        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>
    </div>
</body>
</html>
""".formatted(ownerName, propertyTitle);
    }
    public static String buildRejectedTemplate(String ownerName, String propertyTitle) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .header { background: #6366f1; color: white; padding: 30px; text-align: center; font-size: 24px; font-weight: bold; }
        .content { padding: 30px; color: #334155; line-height: 1.6; }
        .status-box { background: #fef2f2; padding: 20px; border-radius: 8px; border-left: 4px solid #ef4444; margin: 20px 0; }
        .property-info { background: #f8fafc; padding: 15px; border-radius: 8px; border: 1px solid #e2e8f0; margin-top: 10px; }
        .badge { display: inline-block; padding: 4px 12px; background: #ef4444; color: white; border-radius: 20px; font-size: 11px; font-weight: bold; text-transform: uppercase; margin-top: 8px; }
        .footer { text-align: center; font-size: 12px; color: #94a3b8; padding: 25px; background: #f8fafc; border-top: 1px solid #e2e8f0; }
        strong { color: #1e293b; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">RentalHub</div>
        <div class="content">
            <h2 style="margin-top:0;">Hello %s,</h2>
            <div class="status-box">
                <strong style="color: #991b1b;">Update:</strong> Your property submission was not approved at this time.
            </div>
            <div class="property-info">
                <strong>Property:</strong> %s <br/>
                <span class="badge">Rejected</span>
            </div>
            <p>Please review your listing details to ensure they meet our quality guidelines. You can update the information and resubmit for approval anytime.</p>
        </div>
        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>
    </div>
</body>
</html>
""".formatted(ownerName, propertyTitle);
    }
    public static String buildResetLink(String name, String resetLink) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .header { background: #6366f1; color: white; padding: 30px; text-align: center; font-size: 24px; font-weight: bold; }
        .content { padding: 40px 30px; color: #334155; line-height: 1.6; text-align: center; }
        .btn { display: inline-block; padding: 14px 28px; background: #6366f1; color: #ffffff !important; text-decoration: none; border-radius: 8px; font-weight: 600; margin: 25px 0; box-shadow: 0 4px 6px rgba(99, 102, 241, 0.2); }
        .footer { text-align: center; font-size: 12px; color: #94a3b8; padding: 25px; background: #f8fafc; border-top: 1px solid #e2e8f0; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">RentalHub</div>
        <div class="content">
            <h2 style="margin-top:0;">Hello %s,</h2>
            <p>We received a request to reset your password. If you didn't make this request, you can safely ignore this email.</p>
            <a href="%s" class="btn">Reset Password</a>
            <p style="font-size: 13px; color: #64748b;">This link will expire in 24 hours.</p>
        </div>
        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>
    </div>
</body>
</html>
""".formatted(name, resetLink);
    }
    public static String buildAcceptBookingTemplate(String tenantName, String ownerName, String propertyTitle) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .header { background: #6366f1; color: white; padding: 30px; text-align: center; font-size: 24px; font-weight: bold; }
        .content { padding: 30px; color: #334155; line-height: 1.6; }
        .status-box { background: #f0fdf4; padding: 20px; border-radius: 8px; border-left: 4px solid #22c55e; margin: 20px 0; }
        .property-info { background: #f8fafc; padding: 15px; border-radius: 8px; border: 1px solid #e2e8f0; margin-top: 10px; }
        .badge { display: inline-block; padding: 4px 12px; background: #22c55e; color: white; border-radius: 20px; font-size: 11px; font-weight: bold; text-transform: uppercase; margin-top: 8px; }
        .footer { text-align: center; font-size: 12px; color: #94a3b8; padding: 25px; background: #f8fafc; border-top: 1px solid #e2e8f0; }
        strong { color: #1e293b; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">RentalHub</div>
        <div class="content">
            <h2 style="margin-top:0;">Hello %s,</h2>
            <div class="status-box">
                <strong style="color: #166534;">Great News!</strong> Your booking request has been accepted by the owner.
            </div>
            <div class="property-info">
                <strong>Property:</strong> %s <br/>
                <strong>Owner:</strong> %s <br/>
                <span class="badge">Booking Confirmed</span>
            </div>
            <p>You can now proceed with the next steps as outlined in your dashboard. Feel free to contact the owner if you have any specific questions about your move-in.</p>
        </div>
        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>
    </div>
</body>
</html>
""".formatted(tenantName, propertyTitle, ownerName);
    }
    public static String buildRejectBookingTemplate(String tenantName, String ownerName, String propertyTitle) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .header { background: #6366f1; color: white; padding: 30px; text-align: center; font-size: 24px; font-weight: bold; }
        .content { padding: 30px; color: #334155; line-height: 1.6; }
        .status-box { background: #fef2f2; padding: 20px; border-radius: 8px; border-left: 4px solid #ef4444; margin: 20px 0; }
        .property-info { background: #f8fafc; padding: 15px; border-radius: 8px; border: 1px solid #e2e8f0; margin-top: 10px; }
        .badge { display: inline-block; padding: 4px 12px; background: #ef4444; color: white; border-radius: 20px; font-size: 11px; font-weight: bold; text-transform: uppercase; margin-top: 8px; }
        .footer { text-align: center; font-size: 12px; color: #94a3b8; padding: 25px; background: #f8fafc; border-top: 1px solid #e2e8f0; }
        strong { color: #1e293b; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">RentalHub</div>
        <div class="content">
            <h2 style="margin-top:0;">Hello %s,</h2>
            <div class="status-box">
                <strong style="color: #991b1b;">Booking Update:</strong> Your booking request for the property below was not accepted.
            </div>
            <div class="property-info">
                <strong>Property:</strong> %s <br/>
                <strong>Owner:</strong> %s <br/>
                <span class="badge">Declined</span>
            </div>
            <p>Don't worry! There are many other great properties available on RentalHub. We recommend browsing similar listings in that area to find your next home.</p>
        </div>
        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>
    </div>
</body>
</html>
""".formatted(tenantName, propertyTitle, ownerName);
    }
    public static String buildOtpEmailTemplate(String otp) {

        if (otp == null || otp.length() != 6) {
            throw new IllegalArgumentException("OTP must be exactly 6 digits");
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f1f5f9;
                        margin: 0;
                        padding: 0;
                        text-align: center;
                    }
                    .container {
                        max-width: 500px;
                        margin: 50px auto;
                        background: #ffffff;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
                    }
                    .otp-box {
                        display: inline-block;
                        width: 45px;
                        height: 55px;
                        line-height: 55px;
                        margin: 5px;
                        font-size: 24px;
                        font-weight: bold;
                        border: 2px solid #6366f1;
                        border-radius: 8px;
                        background: #f8fafc;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Your Verification Code</h2>

                    <div>
                        <span class="otp-box">%s</span>
                        <span class="otp-box">%s</span>
                        <span class="otp-box">%s</span>
                        <span class="otp-box">%s</span>
                        <span class="otp-box">%s</span>
                        <span class="otp-box">%s</span>
                    </div>

                </div>
            </body>
            </html>
            """.formatted(
                otp.charAt(0),
                otp.charAt(1),
                otp.charAt(2),
                otp.charAt(3),
                otp.charAt(4),
                otp.charAt(5)
        );
    }
    public static String buildEmailVerifyTemplate(String token) {

        // Validate token (must be 6 digits)
        if (token == null || !token.matches("\\d{6}")) {
            throw new IllegalArgumentException("Token must be exactly 6 digits");
        }

        // Build OTP boxes dynamically
        StringBuilder tokenBoxes = new StringBuilder();
        for (char digit : token.toCharArray()) {
            tokenBoxes.append("<span class=\"otp-box\">")
                    .append(digit)
                    .append("</span>");
        }

        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #f1f5f9;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            background: #ffffff;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
        }

        .header {
            background: #6366f1;
            color: white;
            padding: 30px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }

        .content {
            padding: 30px;
            color: #334155;
            line-height: 1.6;
        }

        .verify-box {
            background: #eef2ff;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #6366f1;
            margin: 20px 0;
        }

        .otp-container {
            margin: 20px 0;
            text-align: center;
        }

        .otp-box {
            display: inline-block;
            width: 48px;
            height: 58px;
            line-height: 58px;
            margin: 6px;
            font-size: 26px;
            font-weight: bold;
            border-radius: 10px;
            background: #f8fafc;
            border: 2px solid #6366f1;
            color: #111827;
        }

        .code-text {
            text-align: center;
            font-size: 18px;
            font-weight: bold;
            letter-spacing: 4px;
            color: #6366f1;
            margin-top: 10px;
        }

        .info-box {
            background: #f8fafc;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
            margin-top: 10px;
            text-align: center;
        }

        .badge {
            display: inline-block;
            padding: 4px 12px;
            background: #6366f1;
            color: white;
            border-radius: 20px;
            font-size: 11px;
            font-weight: bold;
            text-transform: uppercase;
            margin-top: 8px;
        }

        .footer {
            text-align: center;
            font-size: 12px;
            color: #94a3b8;
            padding: 25px;
            background: #f8fafc;
            border-top: 1px solid #e2e8f0;
        }

        strong {
            color: #1e293b;
        }

    </style>
</head>
<body>

    <div class="container">

        <div class="header">
            RentalHub
        </div>

        <div class="content">

            <h2 style="margin-top:0;">Email Verification</h2>

            <div class="verify-box">
                <strong>Action Required:</strong> Please use the verification code below to confirm your email address.
            </div>

            <div class="otp-container">
                %s
            </div>

            <div class="code-text">
                %s
            </div>

            <div class="info-box">
                This code will expire in <strong>5 minutes</strong>.<br/>
                <span class="badge">Secure Code</span>
            </div>

            <p style="margin-top:20px;">
                If you didn’t request this, you can safely ignore this email.
            </p>

        </div>

        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>

    </div>

</body>
</html>
""".formatted(tokenBoxes.toString(), token);
    }
}